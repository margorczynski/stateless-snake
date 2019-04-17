package logic.input

import logic.GameState

trait GameInput[T] {
  def handleInput: T => GameState => GameState
}