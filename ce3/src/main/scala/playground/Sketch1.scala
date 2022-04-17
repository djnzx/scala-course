package playground

import cats.implicits._
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.duration.DurationInt

object Sketch1 extends IOApp.Simple {

  def dots: IO[Nothing] = (IO(print(".")) >> IO.sleep(500.millis)) >> dots
  def divs: IO[Nothing] = (IO(print("#")) >> IO.sleep(2000.millis)) >> divs

  val app =
    (dots, divs).parTupled.timeout(10.seconds).handleErrorWith { _ => IO.print("\nDone!") }

  override def run: IO[Unit] = app.void
}
