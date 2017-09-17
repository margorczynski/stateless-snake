package logic

import logic.Snake.PositionWithDirection

case class Snake(positionsWithDirections: Seq[PositionWithDirection])

object Snake {
  type PositionWithDirection = (Position, Direction)
}