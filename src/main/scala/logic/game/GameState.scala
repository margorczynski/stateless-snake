package logic.game

sealed trait GameState
  case class Running(foodPosition: Position,
                     snake: Snake,
                     seed: Long,
                     mapSize: Int,
                     snakeMovementTimer: SnakeMovementTimer) extends GameState
  case class Paused(stateBeforePause: Running)               extends GameState
  case class Exited(reason: ExitReason)                      extends GameState
