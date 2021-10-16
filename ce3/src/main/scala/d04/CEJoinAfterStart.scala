package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Outcome
import d01.debug.DebugHelper

import scala.concurrent.duration.DurationInt

object CEJoinAfterStart extends IOApp {

  val task: IO[String] = IO.sleep(2.seconds) *> IO("task").debug

  /** explicitly emphasize, that forked thing can fail */
  val joined: IO[Outcome[IO, Throwable, String]] = for {
    fiber <- task.start
    // ...
    s <- fiber.join
  } yield s

  val app = for {
    fiber <- task.start // line 2
    _ <- IO("pre-join").debug // line 1
    _ <- fiber.join.debug // line 3. joined with result calculated
    _ <- IO("post-join").debug
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
