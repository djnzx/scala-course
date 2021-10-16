package d05

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import d01.debug.DebugHelper

object ShiftingApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("one").debug
      _ <- IO("two").debug
      _ <- IO("three").debug
    } yield ExitCode.Success

}
