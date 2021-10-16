package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.duration.DurationInt

object TickingClock extends IOApp {

  val getCurrent: IO[Long] = IO(System.currentTimeMillis)
  val printCurrent: Long => IO[Unit] = (t: Long) => IO(println(t))
  val sleep: IO[Unit] = IO.sleep(1.second)

  /** to use in for-comprehensions - everything must be lifted to IO */
  def tickingClockN(n: Int): IO[Unit] = n match {
    case 0 => IO { () }
    case _ =>
      for {
        t <- getCurrent
        _ <- printCurrent(t)
        _ <- sleep
        _ <- tickingClockN(n - 1)
      } yield ()
  }

  def run(args: List[String]): IO[ExitCode] =
    tickingClockN(5)
      .as(ExitCode.Success)

}
