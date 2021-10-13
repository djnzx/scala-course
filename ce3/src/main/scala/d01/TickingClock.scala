package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.duration.DurationInt

object TickingClock extends IOApp {

  val getCurrent: IO[Long] = IO(System.currentTimeMillis)
  val printCurrent: Long => IO[Unit] = (t: Long) => IO(println(t))
  val sleep: IO[Unit] = IO.sleep(1.second)

  val tickingClock: IO[Unit] = for {
    t <- getCurrent
    _ <- printCurrent(t)
    _ <- sleep
    _ <- tickingClock
  } yield ()

  def run(args: List[String]): IO[ExitCode] =
    tickingClock.as(ExitCode.Success)

}
