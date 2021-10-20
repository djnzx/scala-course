package ce2.d04_concurrent_control

import cats.effect._
import cats.implicits._
import ce2.common.debug.DebugHelper

object Cancel extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      fiber <- task.start // <2>
      _ <- IO("pre-cancel").debug
      // <3>
      _ <- IO("canceled").debug
    } yield ExitCode.Success

  val task: IO[Nothing] =
    IO("task").debug *>
      IO.never // <1>
}
