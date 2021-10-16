package d05

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import d01.debug.DebugHelper

object BlockingApp extends IOApp {

  /** debug works wrong on IO.blocking !!!
    */
  val withBlocker: IO[Unit] = for {
    _ <- IO("on default").debug
    _ <- IO.blocking("on blocker").debug /* WRONG !!! */
    _ <- IO("where am I1").debug
    _ <- IO("where am I2").debug
  } yield ()

  val app = withBlocker

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)
}
