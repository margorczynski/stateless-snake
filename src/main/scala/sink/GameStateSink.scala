package sink

import akka.stream.scaladsl.Sink

object GameStateSink {
  def getGameStateSink = Sink.foreach(println)
}