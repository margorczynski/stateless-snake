package logic

sealed trait GameState
case class  Running(foodPosition: Position,
                    snakePosition: Seq[Position],
                    snakeDirection: Direction,
                    seed: Long)                   extends GameState
case class  Paused(stateBeforePause: Running)     extends GameState
case object Exited                                extends GameState