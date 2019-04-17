package logic.input
import logic._

sealed trait SnakeGameInput
  case class  KeyboardInput(key: Key) extends SnakeGameInput
  case object ClockInput              extends SnakeGameInput

object SnakeGameInput {

  implicit val snakeGameInputIsGameInput: GameInput[SnakeGameInput] =
    new GameInput[SnakeGameInput] {
      def handleInput: SnakeGameInput => GameState => GameState = input => gameState => input match {

        case ClockInput =>
          gameState match {
            case gsr @ Running(_, _, _, _, clockTicks, _) =>
              gsr.copy(clockTicks = clockTicks + 1)
            case _ =>
              gameState
          }

        case KeyboardInput(key) =>
          gameState match {
            case Exited =>
              Exited
            case Paused(state) => key match {
              case Space  => state
              case Escape => Exited
            }
            case gsr @ Running(_, snake, _, _, _, _) =>
              gsr.copy(snake = snake.copy(direction = getNewDirection(snake.direction, key)))
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
      case Right => Right
      case _     => Left
    }
    case KeyD => currentDirection match {
      case Left => Left
      case _    => Right
    }
    case _    => currentDirection
  }
}