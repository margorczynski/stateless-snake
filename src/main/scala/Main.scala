import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import flow.GameFlow
import logic.game.GameState
import logic.input.SnakeGameInput
import sink.GameStateSink
import source.{ClockSource, InitialGameStateSource, KeyboardSource}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem  = ActorSystem("stateless-snake-actor-system")
    implicit val materializer = ActorMaterializer()

    val gameGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      //Log the elements coming out of the Sources
      val keyboardSource         = KeyboardSource.getKeyboardSource.log("keyboard-source-log")
      val clockSource            = ClockSource.getClockSource.log("clock-source-log")
      val initialGameStateSource = InitialGameStateSource.getInitialGameStateSource.log("initial-game-state-source-log")

      val gameStateSink = GameStateSink.getGameStateSink

      //Log the elements coming out of the Game Flow
      val gameFlow = GameFlow.getGameFlow.log("game-flow-log")

      val inputWithGameStateZip = builder.add(Zip[SnakeGameInput, GameState]())
      val gameInputMerge        = builder.add(Merge[SnakeGameInput](2))
      val gameStateMerge        = builder.add(Merge[GameState](2))
      val gameStateBroadcast    = builder.add(Broadcast[GameState](2))

      keyboardSource ~> gameInputMerge.in(0)
      clockSource    ~> gameInputMerge.in(1)

      //Zipping the input with the game state
      gameInputMerge ~> inputWithGameStateZip.in0
      gameStateMerge ~> inputWithGameStateZip.in1

      //Merging the initial game state source with the broadcast with the previous game state
      initialGameStateSource    ~> gameStateMerge.in(0)
      gameStateBroadcast        ~> gameStateMerge.in(1)
      inputWithGameStateZip.out ~> gameFlow             ~> gameStateBroadcast

      gameStateBroadcast ~> gameStateSink

      ClosedShape
    })

    gameGraph.run()
  }
}