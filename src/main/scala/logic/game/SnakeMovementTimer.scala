package logic.game

case class SnakeMovementTimer(tickCount: Long = 2) {

  def callback: Running => Running = {

    case gsr @ Running(_, Snake(segmentPositions, direction), _, _, _) =>
      val snakeHeadPosition = segmentPositions.head

      val newHeadPosition = {
        direction match {
          case Up    => Position(snakeHeadPosition.x, snakeHeadPosition.y - 1)
          case Down  => Position(snakeHeadPosition.x, snakeHeadPosition.y + 1)
          case Left  => Position(snakeHeadPosition.x - 1, snakeHeadPosition.y)
          case Right => Position(snakeHeadPosition.x + 1, snakeHeadPosition.y)
        }
      }

      val newSegmentPositions = newHeadPosition +: segmentPositions.init

      // Replace the ran-out timer with a new one as this action should be recurrent
      gsr.copy(snake = Snake(newSegmentPositions, direction), snakeMovementTimer = SnakeMovementTimer())
  }
}