package source

import akka.NotUsed
import akka.stream.scaladsl.Source
import logic._
import logic.game._

object InitialGameStateSource {
  def getInitialGameStateSource: Source[GameState, NotUsed] = Source.single(
    Running(
      foodPosition = initialFoodPosition,
      snake = initialSnake,
      seed = initialSeed,
      mapSize = mapSize,
      snakeMovementTimer = SnakeMovementTimer())
  )

  //TODO: Generate an initial "random" state within the GameStateEngine?
  private val initialFoodPosition = Position(25, 25)
  private val initialSnake        = Snake(Seq(Position(40, 40), Position(40, 39)), Up)
  private val initialSeed         = System.currentTimeMillis()

  private val mapSize = 50
}