package _playground

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import scala.concurrent.TimeoutException
import scala.concurrent.duration.DurationInt

object Timeout extends IOApp.Simple {

  def out(s: String): IO[Unit] = IO.blocking(print(s))

  def dots: IO[Nothing] = out(".") >> IO.sleep(500.millis) >> dots
  def divs: IO[Nothing] = out("#") >> IO.sleep(2000.millis) >> divs

  val app =
    (dots, divs).parTupled
      .timeout(10.seconds)
      .void
      .handleErrorWith {
        case x: TimeoutException => out(s"terminated due to timeout: ${x.getMessage}")
        case _                   => new IllegalArgumentException("shouldn't be there").raiseError[IO, Unit]
      }

  override def run: IO[Unit] = app

}
