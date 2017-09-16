package logic

sealed trait GameState
case class  Running(foodPosition: Position, snakePosition: Seq[Position]) extends GameState
case object Paused extends GameState
case object Exited extends GameState