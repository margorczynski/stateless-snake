package logic.input

import logic.game.GameState

trait GameInput[T] {
  def handleInput: T => GameState => GameState
}