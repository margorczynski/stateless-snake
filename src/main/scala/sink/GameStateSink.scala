package sink

import akka.stream.scaladsl.Sink
import logic._
import logic.game._
import org.fusesource.jansi.{Ansi, AnsiConsole}

object GameStateSink {

  private lazy val ansiState = Ansi.ansi()

  private def handleGameState(gameState: GameState): Unit = gameState match {
    case Running(foodPosition, snake, _, _, _) =>
      drawRunningGameState(foodPosition, snake.segmentPositions)
    case Paused(stateBeforePause) =>
      handleGameState(stateBeforePause)
    case Exited(_) =>
  }

  private def drawRunningGameState(foodPosition: Position, snakeSegmentPositions: Seq[Position]): Unit = {
    AnsiConsole.systemInstall()

    ansiState.eraseScreen()

    drawObject('F',foodPosition)
    snakeSegmentPositions.foreach(drawObject('*', _))

    AnsiConsole.out.println(ansiState)
  }

  private def drawObject(objectCharacter: Char, position: Position) =
    ansiState.cursor(position.y, position.x).a(objectCharacter)

  def getGameStateSink = Sink.foreach(handleGameState)
}