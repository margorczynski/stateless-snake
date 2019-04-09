package logic

import source.KeyboardSource._
import source.KeyboardSource.AlphanumericKey

import scala.util.Random

object GameStateEngine {

  def calculate(keyPressed: Option[Key], previousGameState: GameState) = previousGameState match {
    case Exited        => Exited
    case Paused(state) => keyPressed match {
      case Some(key) => key match {
        case Space  => state
        case Escape => Exited
      }
      case None => previousGameState
    }
    case Running(foodPosition, Snake(segmentPositions, direction), seed, mapSize) => keyPressed match {
      case Some(Space)        => Paused(Running(foodPosition, Snake(segmentPositions, direction), seed, mapSize))
      case Some(Escape)       => Exited
      case directionKeyOption =>
        if(ateItself(segmentPositions) || isOutOfBound(mapSize, segmentPositions)) {
          Exited
        } else {
          val newSeed      = generateNewSeed(seed)
          val newDirection = directionKeyOption match {
            case Some(directionKey) => getNewDirection(direction, directionKey)
            case None               => direction
          }
          val newSegmentPositions =  {
            val afterEating       = getSnakePositionsAfterEating(foodPosition, segmentPositions)
            val snakeHeadPosition = segmentPositions.head

            val newHeadPosition = direction match {
              case Up    => Position(snakeHeadPosition.x, snakeHeadPosition.y - 1)
              case Down  => Position(snakeHeadPosition.x, snakeHeadPosition.y + 1)
              case Left  => Position(snakeHeadPosition.x - 1, snakeHeadPosition.y)
              case Right => Position(snakeHeadPosition.x + 1, snakeHeadPosition.y)
            }

            (newHeadPosition +: afterEating).init
          }
          val newFoodPosition = if(ateFood(foodPosition, segmentPositions)) {
            getFirstViableFoodPosition(segmentPositions, seed, mapSize)
          } else foodPosition

          Running(newFoodPosition, Snake(newSegmentPositions, newDirection), newSeed, mapSize)
        }
    }
  }

  //TODO: Can be remade if we encode which Direction type is the opposite of which and mapping
  private def getNewDirection(currentDirection: Direction, keyPressed: Key): Direction = keyPressed match {
    case KeyW => currentDirection match {
      case Down => Down
      case _    => Up
    }
    case KeyS => currentDirection match {
      case Up => Up
      case _  => Down
    }
    case KeyA => currentDirection match {
      case Right => Right
      case _     => Left
    }
    case KeyD => currentDirection match {
      case Left => Left
      case _    => Right
    }
    case _    => currentDirection
  }

  private def getSnakePositionsAfterEating(foodPosition: Position, segmentPositions: Seq[Position]): Seq[Position] = {
    if(ateFood(foodPosition, segmentPositions)) {
      segmentPositions :+ Position()
    } else segmentPositions
  }

  private def getFirstViableFoodPosition(segmentPositions: Seq[Position], seed: Long, mapSize: Int): Position = {
    val generator   = new Random(seed)
    val newPosition = Position(generator.nextInt(mapSize), generator.nextInt(mapSize))

    if(segmentPositions.contains(newPosition))
      getFirstViableFoodPosition(segmentPositions, seed / 2, mapSize)
    else
      newPosition
  }

  private def isOutOfBound(mapSize: Int, segmentPositions: Seq[Position]) =
    segmentPositions.head.x < 0 ||
    segmentPositions.head.x >= mapSize ||
    segmentPositions.head.y < 0 ||
    segmentPositions.head.y >= mapSize

  private def ateFood(foodPosition: Position, segmentPositions: Seq[Position]) = foodPosition == segmentPositions.head

  private def ateItself(snakePosition: Seq[Position]) = snakePosition.tail.contains(snakePosition.head)

  private def generateNewSeed(oldSeed: Long) = (oldSeed * 2) % seedBound

  private val seedBound = 12345L
}