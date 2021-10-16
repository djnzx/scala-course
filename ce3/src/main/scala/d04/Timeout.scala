package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import d01.debug.DebugHelper

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object Timeout extends IOApp {

  val task: IO[Unit] = sleep("task", 100.millis)
  val timeout: IO[Unit] = sleep("timeout", 500.millis)

  def sleep(name: String, duration: FiniteDuration) =
    (IO { s"$name: starting" }.debug *>
      IO.sleep(duration) *>
      IO { s"$name: done" }.debug).onCancel(IO { s"$name: cancelled" }.debug.void).void

  // IO.timeout(t) is no more than IO.race(x, t)
  val app = for {
    done <- IO.race(task, timeout)
    _ <- done match {
      case Left(_)  => IO("task won").debug
      case Right(_) => IO("timeout won").debug
    }
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
