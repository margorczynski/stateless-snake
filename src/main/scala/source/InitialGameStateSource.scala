package source

import akka.NotUsed
import akka.stream.scaladsl.Source
import logic._

object InitialGameStateSource {
  def getInitialGameStateSource: Source[GameState, NotUsed] = Source.single(
    Running(
      foodPosition = initialFoodPosition,
      snake = initialSnake,
      seed = initialSeed,
      mapSize)
  )

  //TODO: Generate an initial "random" state within the GameStateEngine?
  private val initialFoodPosition = Position(10, 10)
  private val initialSnake        = Snake(Seq(Position(20, 20), Position(20, 19)), Up)
  private val initialSeed         = System.currentTimeMillis()

  private val mapSize = 100
}