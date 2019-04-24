package logic.input
import logic.{game, _}
import logic.game._

sealed trait SnakeGameInput
  case class  KeyboardInput(key: Key) extends SnakeGameInput
  case object ClockInput              extends SnakeGameInput

object SnakeGameInput {

  implicit val snakeGameInputIsGameInput: GameInput[SnakeGameInput] =
    new GameInput[SnakeGameInput] {
      def handleInput: SnakeGameInput => GameState => GameState = input => gameState => input match {

        case ClockInput =>
          gameState match {
            case gsr @ Running(_, _, _, _, snakeMovementTimer) =>
              if(snakeMovementTimer.tickCount == 0) {
                snakeMovementTimer.callback(gsr)
              } else gsr.copy(snakeMovementTimer = snakeMovementTimer.copy(tickCount = snakeMovementTimer.tickCount - 1))
            case _ =>
              gameState
          }

        case KeyboardInput(key) =>
          gameState match {
            case Exited(_) =>
              gameState
            case Paused(state) => key match {
              case Space  => state
              case Escape => Exited(EscapePressed)
              case _      => gameState
            }
            case gsr @ Running(_, snake, _, _, _) =>
              key match {
                case Space  => Paused(gsr)
                case Escape => Exited(EscapePressed)
                case _      =>
                  gsr.copy(snake = snake.copy(direction = getNewDirection(snake.direction, key)))
              }
          }
      }
    }

  private def getNewDirection(currentDirection: Direction, key: Key): Direction = key match {
    case KeyW => currentDirection match {
      case Down => Down
      case _    => Up
    }
    case KeyS => currentDirection match {
      case Up => Up
      case _  => Down
    }
    case KeyA => currentDirection match {
      case game.Right => game.Right
      case _     => game.Left
    }
    case KeyD => currentDirection match {
      case game.Left => game.Left
      case _    => game.Right
    }
    case _    => currentDirection
  }
}