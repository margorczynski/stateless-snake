package logic

sealed trait GameState
case class  Running(foodPosition: Position,
                    snake: Snake,
                    seed: Long,
                    mapSize: Int)                 extends GameState
case class  Paused(stateBeforePause: Running)     extends GameState
case object Exited                                extends GameState

object GameState {
  //TODO: This isn't pure
  def apply(): GameState = Running(foodPosition = initialFoodPosition,
                                 snake = initialSnake,
                                 seed = System.currentTimeMillis(),
                                 mapSize)

  //TODO: Generate an initial "random" state within the GameStateEngine?
  private val initialFoodPosition = Position(10, 10)
  private val initialSnake        = Snake(Seq(Position(20, 20), Position(20, 19)), Up)

  private val mapSize = 100
}