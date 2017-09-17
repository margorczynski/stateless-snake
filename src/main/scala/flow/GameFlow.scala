package flow

import akka.NotUsed
import akka.stream.scaladsl.Flow
import logic.{GameState, GameStateEngine}
import source.KeyboardSource.Key

object GameFlow {
  type GameFlowInput = (Option[Key], GameState)

  def getGameFlow: Flow[(Option[Key], GameState), GameState, NotUsed] = {
    Flow.fromFunction(flowCalculate)
  }

  private def flowCalculate(gameFlowInput: GameFlowInput) =
    GameStateEngine.calculate(
      gameFlowInput._1,
      gameFlowInput._2
    )
}