package d05

import cats.effect.Blocker
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import common.debug.DebugHelper

object BlockingApp extends IOApp {

  def withBlocker(b: Blocker): IO[Unit] = for {
    _ <- IO("on default").debug
    _ <- b.blockOn(IO("on blocker").debug)
    _ <- IO("where am I?").debug
  } yield ()

  val app = Blocker[IO].use { b: Blocker =>
    withBlocker(b)
  }

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)
}
