package source

import java.io.{BufferedReader, InputStreamReader}

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import logic.input._

object KeyboardSource {

  def getKeyboardSource: Source[KeyboardInput, NotUsed] = {
    val consoleReader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))

    def currentlyPressedKey = if(consoleReader.ready()) Some(consoleReader.read()) else None

    Source.fromIterator( () =>
      Iterator.continually(currentlyPressedKey.map(getKey))
    ).via(Flow[Option[Key]].collect {
      case Some(key) =>
        KeyboardInput(key)
    })
  }

  private def getKey(asciiCode: Int) = asciiCode match {
    case KeyW.asciiCode   => KeyW
    case KeyS.asciiCode   => KeyS
    case KeyA.asciiCode   => KeyA
    case KeyD.asciiCode   => KeyD
    case Space.asciiCode  => Space
    case Escape.asciiCode => Escape
  }
}