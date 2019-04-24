package logic.game

import logic.input.{GameInput, _}

import scala.util.Random

object GameStateEngine {

  def calculate(snakeGameInput: SnakeGameInput, previousGameState: GameState): GameState =
    (implicitly[GameInput[SnakeGameInput]].handleInput(snakeGameInput) andThen handleGameLogic)(previousGameState)

  private def handleGameLogic(gameState: GameState): GameState = gameState match {
    case Running(foodPosition, Snake(segmentPositions, direction), seed, mapSize, snakeMovementTimer) =>
      if(ateItself(segmentPositions)) {
        Exited(AteItself)
      } else if (isOutOfBound(mapSize, segmentPositions)) {
        Exited(OutOfBounds)
      } else {
        val newSeed             = generateNewSeed(seed)
        val newSegmentPositions = getSnakePositionsAfterEating(foodPosition, segmentPositions)
        val newFoodPosition     = getNewFoodPosition(foodPosition, segmentPositions, seed, mapSize)

        Running(newFoodPosition, Snake(newSegmentPositions, direction), newSeed, mapSize, snakeMovementTimer)
      }
    case _ =>
      gameState
  }

  private def getSnakePositionsAfterEating(foodPosition: Position, segmentPositions: Seq[Position]): Seq[Position] = {
    if(ateFood(foodPosition, segmentPositions)) {
      segmentPositions :+ Position()
    } else segmentPositions
  }

  private def getNewFoodPosition(foodPosition: Position, segmentPositions: Seq[Position], seed: Long, mapSize: Int) ={
    if(ateFood(foodPosition, segmentPositions)) {
      getFirstViableFoodPosition(segmentPositions, seed, mapSize)
    } else foodPosition
  }

  private def getFirstViableFoodPosition(segmentPositions: Seq[Position], seed: Long, mapSize: Int): Position = {
    val generator   = new Random(seed)
    val newPosition = Position(generator.nextInt(mapSize), generator.nextInt(mapSize))

    // If the generated new food position is taken by the snake find a new one using a different seed
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