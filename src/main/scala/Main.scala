import java.io.{BufferedReader, InputStreamReader}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import flow.GameFlow
import logic.GameState
import sink.GameStateSink
import source.KeyboardSource.Key
import source.{ClockSource, InitialGameStateSource, KeyboardSource}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem  = ActorSystem("stateless-snake-actor-system")
    implicit val materializer = ActorMaterializer()

    val gameGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      val keyboardSource         = KeyboardSource.getKeyboardSource
      val clockSource            = ClockSource.getClockSource
      val initialGameStateSource = InitialGameStateSource.getInitialGameStateSource

      val gameStateSink = GameStateSink.getGameStateSink

      val mapToKeyFlow = Flow[(Unit, Option[Key])].map { case (_, key) => key }
      val gameFlow     = GameFlow.getGameFlow

      val synchronizationZip  = builder.add(Zip[Unit, Option[Key]]())
      val keyWithGameStateZip = builder.add(Zip[Option[Key], GameState]())
      val gameStateMerge      = builder.add(Merge[GameState](2))
      val gameStateBroadcast  = builder.add(Broadcast[GameState](2))

      //The stochastic variable sources
      clockSource    ~> synchronizationZip.in0
      keyboardSource ~> synchronizationZip.in1

      //Zipping the input with the game clock
      synchronizationZip.out ~> mapToKeyFlow ~> keyWithGameStateZip.in0
      gameStateMerge         ~> keyWithGameStateZip.in1

      //Merging the initial game state source with the broadcast with the previous game state
      initialGameStateSource  ~> gameStateMerge.in(0)
      gameStateBroadcast      ~> gameStateMerge.in(1)
      keyWithGameStateZip.out ~> gameFlow ~> gameStateBroadcast

      gameStateBroadcast ~> gameStateSink

      ClosedShape
    })

    gameGraph.run()
  }
}