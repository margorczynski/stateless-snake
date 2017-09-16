package flow

import akka.stream.scaladsl.Flow

object GameFlow {
  def getGameFlow(initialSeed: Long) = {
    Flow.fromFunction()
  }
}