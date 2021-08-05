import zio._
import zio.console.Console

import java.io.IOException

object Z0App extends scala.App {

  val program: ZIO[Console, IOException, Unit] = for {
    _ <- console.putStrLn("hello")
  } yield ()

  Runtime.default.unsafeRunSync(program)
}
