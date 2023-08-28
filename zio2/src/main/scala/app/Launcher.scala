package app

import java.io.IOException
import zio._

/** https://zio.dev/guides/
  */
object Launcher extends ZIOAppDefault {

  def run: ZIO[Any, IOException, Unit] =
    Console.printLine("Hello, World!")

}
