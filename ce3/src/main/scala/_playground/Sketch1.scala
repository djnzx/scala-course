package _playground

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import scala.concurrent.TimeoutException
import scala.concurrent.duration.DurationInt

object Sketch1 extends IOApp.Simple {

  def out(s: String): IO[Unit] = IO(print(s))

  def dots: IO[Nothing] = (out(".") >> IO.sleep(500.millis)) >> dots
  def divs: IO[Nothing] = (out("#") >> IO.sleep(2000.millis)) >> divs

  val app =
    (dots, divs).parTupled
      .timeout(10.seconds)
      .handleErrorWith {
        case x: TimeoutException => out(s"\ninterrupted after ${x.getMessage}")
        case _                   => sys.error("shouldn't be there")
      }

  override def run: IO[Unit] = app.void
}
