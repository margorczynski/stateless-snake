import java.io.{BufferedReader, InputStreamReader}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import source.{ClockSource, KeyboardSource}
import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem  = ActorSystem("stateless-snake-actor-system")
    implicit val materializer = ActorMaterializer()

    val keyboardSource = KeyboardSource.getKeyboardSource

    val clockSource = ClockSource.getClockSource

    //keyboardSource.runWith(Sink.foreach(println))
    clockSource.runWith(Sink.foreach(println))

  }
}