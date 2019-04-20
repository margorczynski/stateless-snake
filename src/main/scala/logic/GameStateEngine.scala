package logic

import logic.input._
import logic.input.GameInput

import scala.util.Random

object GameStateEngine {

  def calculate(snakeGameInput: SnakeGameInput, previousGameState: GameState): GameState =
    (implicitly[GameInput[SnakeGameInput]].handleInput(snakeGameInput) andThen handleGameLogic)(previousGameState)

  private def handleGameLogic(gameState: GameState): GameState = gameState match {
    case Running(foodPosition, Snake(segmentPositions, direction), seed, mapSize, clockTicks, lastClockTickMoved) =>
      if(ateItself(segmentPositions)) {
        Exited(AteItself)
      } else if (isOutOfBound(mapSize, segmentPositions)) {
        Exited(OutOfBounds)
      } else {
        val newSeed               = generateNewSeed(seed)
        val isPositionUpdated     = clockTicks != lastClockTickMoved
        val newLastMovedClockTick = if(isPositionUpdated) clockTicks else lastClockTickMoved
        val newSegmentPositions   = if(isPositionUpdated) {
          val afterEating       = getSnakePositionsAfterEating(foodPosition, segmentPositions)
          val snakeHeadPosition = segmentPositions.head

          val newHeadPosition = {
              direction match {
                case Up    => Position(snakeHeadPosition.x, snakeHeadPosition.y - 1)
                case Down  => Position(snakeHeadPosition.x, snakeHeadPosition.y + 1)
                case Left  => Position(snakeHeadPosition.x - 1, snakeHeadPosition.y)
                case Right => Position(snakeHeadPosition.x + 1, snakeHeadPosition.y)
              }
          }

          (newHeadPosition +: afterEating).init
        } else segmentPositions

        val newFoodPosition = if(ateFood(foodPosition, segmentPositions)) {
          getFirstViableFoodPosition(segmentPositions, seed, mapSize)
        } else foodPosition

        Running(newFoodPosition, Snake(newSegmentPositions, direction), newSeed, mapSize, clockTicks, newLastMovedClockTick)
      }
    case _ =>
      gameState
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

  private def ateFood(foodPosition: Position, segmentPositions: Seq[Position]) =
    foodPosition == segmentPositions.head

  private def ateItself(snakePosition: Seq[Position]) =
    snakePosition.tail.contains(snakePosition.head)

  private def generateNewSeed(oldSeed: Long) =
    (oldSeed * 2) % seedBound

  private val seedBound = 12345L
}