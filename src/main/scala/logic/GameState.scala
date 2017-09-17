package logic

sealed trait GameState
case class  Running(foodPosition: Position,
                    snake: Snake,
                    seed: Long,
                    mapSize: Int)                 extends GameState
case class  Paused(stateBeforePause: Running)     extends GameState
case object Exited                                extends GameState