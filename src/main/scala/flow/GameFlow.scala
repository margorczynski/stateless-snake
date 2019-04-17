package flow

import akka.NotUsed
import akka.stream.scaladsl.Flow
import logic.input.SnakeGameInput
import logic.{GameState, GameStateEngine}

object GameFlow {

  type GameFlowInput = (SnakeGameInput, GameState)

  def getGameFlow: Flow[GameFlowInput, GameState, NotUsed] = {
    Flow.fromFunction(flowCalculate)
  }

  private def flowCalculate(gameFlowInput: GameFlowInput) =
    GameStateEngine.calculate(
      gameFlowInput._1,
      gameFlowInput._2
    )
}