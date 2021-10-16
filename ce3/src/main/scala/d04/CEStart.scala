package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import d01.debug.DebugHelper

object CEStart extends IOApp {

  val task: IO[String] = IO("task").debug

  val app = for {
    fiber <- task.start // shift to another thread. the only things fiber can do are: cancel and join
    _ <- IO("task was started").debug
    _ <- fiber.join
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
