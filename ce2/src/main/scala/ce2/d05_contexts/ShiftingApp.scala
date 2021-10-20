package ce2.d05_contexts

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import ce2.common.debug.DebugHelper

object ShiftingApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("one").debug
      _ <- IO.shift
      _ <- IO("two").debug
      _ <- IO.shift
      _ <- IO("three").debug
    } yield ExitCode.Success

}
