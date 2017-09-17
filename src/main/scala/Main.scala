import java.io.{BufferedReader, InputStreamReader}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import logic.GameState
import sink.GameStateSink
import source.KeyboardSource.Key
import source.{ClockSource, KeyboardSource}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem  = ActorSystem("stateless-snake-actor-system")
    implicit val materializer = ActorMaterializer()

    val gameGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      val keyboardSource         = KeyboardSource.getKeyboardSource
      val clockSource            = ClockSource.getClockSource
      //TODO: This probably should be merged with the output of the GameStateFlow
      val initialGameStateSource = Source.single(GameState())

      val gameStateSink = GameStateSink.getGameStateSink

      val mapToKeyFlow = Flow[(Unit, Option[Key])].map { case (_, key) => key }

      val synchronizationZip = builder.add(Zip[Unit, Option[Key]]())

      clockSource    ~> synchronizationZip.in0
      keyboardSource ~> synchronizationZip.in1

      synchronizationZip.out ~> mapToKeyFlow ~> gameStateSink

      ClosedShape
    })

    gameGraph.run()
  }
}