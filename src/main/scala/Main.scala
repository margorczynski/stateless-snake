import java.io.{BufferedReader, InputStreamReader}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import source.{ClockSource, KeyboardSource}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem  = ActorSystem("stateless-snake-actor-system")
    implicit val materializer = ActorMaterializer()

    val keyboardSource = KeyboardSource.getKeyboardSource

    val clockSource = ClockSource.getClockSource

    val aa = clockSource.zip(keyboardSource).map { case (_, key) => key }

    val sink = Sink.foreach(println)

    aa.runWith(sink)
  }
}