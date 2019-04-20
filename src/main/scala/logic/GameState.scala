package logic

sealed trait GameState
  case class Running(foodPosition: Position,
                     snake: Snake,
                     seed: Long,
                     mapSize: Int,
                     clockTicks: Long,
                     lastClockTickMoved: Long) extends GameState
  case class Paused(stateBeforePause: Running) extends GameState
  case class Exited(reason: ExitReason)        extends GameState
