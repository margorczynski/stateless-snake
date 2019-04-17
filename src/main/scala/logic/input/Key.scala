package logic.input

sealed trait Key { val asciiCode: Int }
  sealed trait AlphanumericKey extends Key
    case object  KeyW  extends AlphanumericKey { val asciiCode = 119 }
    case object  KeyS  extends AlphanumericKey { val asciiCode = 115 }
    case object  KeyA  extends AlphanumericKey { val asciiCode = 97 }
    case object  KeyD  extends AlphanumericKey { val asciiCode = 100 }
  sealed trait ModifierKey extends Key
    case object Space  extends ModifierKey { val asciiCode = 32 }
    case object Escape extends ModifierKey { val asciiCode = 27 }