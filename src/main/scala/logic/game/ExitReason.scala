package logic.game

sealed trait ExitReason
case object EscapePressed extends ExitReason
case object AteItself     extends ExitReason
case object OutOfBounds   extends ExitReason