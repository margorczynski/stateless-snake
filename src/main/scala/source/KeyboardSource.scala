package source

import java.io.{BufferedReader, InputStreamReader}

import akka.NotUsed
import akka.stream.scaladsl.Source

object KeyboardSource {

  sealed trait Key { val asciiCode: Int }
    sealed trait AlphanumericKey extends Key
      case object  KeyW  extends AlphanumericKey { val asciiCode = 119 }
      case object  KeyS  extends AlphanumericKey { val asciiCode = 115 }
      case object  KeyA  extends AlphanumericKey { val asciiCode = 97 }
      case object  KeyD  extends AlphanumericKey { val asciiCode = 100 }
    sealed trait ModifierKey extends Key
      case object Space  extends ModifierKey { val asciiCode = 32 }
      case object Escape extends ModifierKey { val asciiCode = 27 }


  def getKeyboardSource: Source[Key, NotUsed] = {
    val consoleReader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))

    Source.fromIterator( () =>
      Iterator.continually(consoleReader.read())
    ).map {
      case KeyW.asciiCode   => KeyW
      case KeyS.asciiCode   => KeyS
      case KeyA.asciiCode   => KeyA
      case KeyD.asciiCode   => KeyD
      case Space.asciiCode  => Space
      case Escape.asciiCode => Escape
    }
  }
}