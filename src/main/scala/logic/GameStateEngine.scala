package logic

import source.KeyboardSource._
import source.KeyboardSource.AlphanumericKey

object GameStateEngine {

  def calculate(initialSeed: Long, keyPressed: Option[Key], previousGameState: GameState) = previousGameState match {
    case Exited        => Exited
    case Paused(state) => keyPressed map {
      case Space  => state
      case Escape => Exited
    }
    case Running(foodPosition, snakePosition, snakeDirection, seed) => keyPressed map {
      case Space        => Paused(Running(foodPosition, snakePosition, snakeDirection, seed))
      case Escape       => Exited
      case directionKey =>
        if(ateItself(snakePosition)) {
          Exited
        } else {
          val newSeed      = seed / 2
          val newSnakeDirection = getNewDirection(snakeDirection, directionKey)
          val newSnakePosition =  {
            if(ateFood(foodPosition, snakePosition)) {
              //TODO: Extend the snake
            }
            //TODO: Move the snake
          }
          val newFoodPosition = if(ateFood(foodPosition, snakePosition)) {
            //TODO: Change the position of the food using the seed and rng
          }

          //TODO: Return the new Running game state (with a new seed)
          Running(newFoodPosition, newSnakePosition, newSnakeDirection, newSeed)
        }

    }
  }

  //TODO: Can be remade if we encode which Direction type is the opposite of which and mapping
  private def getNewDirection(currentDirection: Direction, keyPressed: Key): Direction = keyPressed match {
    case KeyW => if (currentDirection != Down) Up
    case KeyS => if (currentDirection != Up) Down
    case KeyA => if (currentDirection != Right) Left
    case KeyD => if (currentDirection != Left) Right
    case _    => currentDirection
  }

  private def ateFood(foodPosition: Position, snakePosition: Seq[Position]) = foodPosition == snakePosition.head

  private def ateItself(snakePosition: Seq[Position]) = snakePosition.tail.contains(snakePosition.head)
}