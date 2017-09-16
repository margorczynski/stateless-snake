package source

import akka.NotUsed
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source

import scala.concurrent.duration._

object ClockSource {
  def getClockSource: Source[Unit, NotUsed] = {
    internalClockSource
  }

  private val internalClockSource = Source.repeat(()).throttle(1, 1 second, 1, ThrottleMode.shaping)
}