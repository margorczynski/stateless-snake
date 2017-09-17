package logic

import source.KeyboardSource._
import source.KeyboardSource.AlphanumericKey

import scala.util.Random

object GameStateEngine {

  def calculate(initialSeed: Long, keyPressed: Option[Key], previousGameState: GameState) = previousGameState match {
    case Exited        => Exited
    case Paused(state) => keyPressed map {
      case Space  => state
      case Escape => Exited
    }
    case Running(foodPosition, snake, seed, mapSize) => keyPressed map {
      case Space        => Paused(Running(foodPosition, snake, seed, mapSize))
      case Escape       => Exited
      case directionKey =>
        val snakePosition   = snake.positionsWithDirections.map(_._1)
        val snakeDirections = snake.positionsWithDirections.map(_._2)
        if(ateItself(snakePosition)) {
          Exited
        } else {
          val newSeed      = seed / 2
          val newSnakeDirection = getNewDirection(snakeDirections.head, directionKey)
          val newSnakePosition =  {
            val afterEating = getSnakeAfterEating(foodPosition, snake)

            //TODO: Move the snake and change directions with fold?
            afterEating.positionsWithDirections.reduceRight((left, right) => )
          }
          val newFoodPosition = if(ateFood(foodPosition, snakePosition)) {
            getFirstViableFoodPosition(snake, seed, mapSize)
          } else foodPosition

          //TODO: Return the new Running game state (with a new seed)
          Running(newFoodPosition, newSnake, newSeed, mapSize)
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

  private def getPositionWithDirectionOfNewPiece(snake: Snake) = snake.positionsWithDirections.last match {
    case (position, Up)    => (Position(position.x, position.y + 1), Up)
    case (position, Down)  => (Position(position.x, position.y - 1), Down)
    case (position, Left)  => (Position(position.x + 1, position.y), Left)
    case (position, Right) => (Position(position.x - 1, position.y), Right)
  }

  private def getSnakeAfterEating(foodPosition: Position, snake: Snake): Snake = {
    val positions = snake.positionsWithDirections.map(_._1)

    if(ateFood(foodPosition, positions)) {
      Snake(snake.positionsWithDirections :+ getPositionWithDirectionOfNewPiece(snake))
    } else snake
  }

  private def getFirstViableFoodPosition(snake: Snake, seed: Long, mapSize: Int): Position = {
    val generator   = new Random(seed)
    val newPosition = Position(generator.nextInt(mapSize), generator.nextInt(mapSize))

    if(snake.positionsWithDirections.map(_._1).contains(newPosition))
      getFirstViableFoodPosition(snake, seed / 2, mapSize)
    else
      newPosition
  }

  private def ateFood(foodPosition: Position, snakePosition: Seq[Position]) = foodPosition == snakePosition.head

  private def ateItself(snakePosition: Seq[Position]) = snakePosition.tail.contains(snakePosition.head)
}