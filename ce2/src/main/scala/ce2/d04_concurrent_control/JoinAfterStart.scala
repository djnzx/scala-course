package ce2.d04_concurrent_control

import cats.effect._
import cats.implicits._
import ce2.common.debug.DebugHelper

object JoinAfterStart extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      fiber <- task.start // <1>
      _ <- IO("pre-join").debug // <3>
      // <2>
      _ <- IO("post-join").debug // <3>
    } yield ExitCode.Success

  val task: IO[String] =
    ??? // <3>
}
