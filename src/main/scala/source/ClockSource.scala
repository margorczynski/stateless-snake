package source

import akka.NotUsed
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import logic.input.{ClockInput, SnakeGameInput}

import scala.concurrent.duration._

object ClockSource {

  def getClockSource: Source[SnakeGameInput, NotUsed] =
    internalClockSource


  private val internalClockSource =
    Source
      .repeat(ClockInput)
      .throttle(1, 100 millisecond, 1, ThrottleMode.Shaping)
}