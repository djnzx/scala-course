package ce2

import cats.effect.Clock
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Timer
import cats.implicits.catsSyntaxFlatMapOps
import java.util.concurrent.TimeUnit
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import scala.concurrent.duration.DurationLong
import scala.util.Random

object ThrottledRecursion extends IOApp.Simple {

  def app(n: Int, toSleep: Long): IO[Unit] =
    if (n == 0) IO.unit
    else
      for {
        before <- Clock[IO].realTime(TimeUnit.MILLISECONDS)
        _      <- IO(println(s"before: $before"))
        // long api call
        _      <- Timer[IO].sleep(Random.nextLong(1000).millis)
        after  <- Clock[IO].realTime(TimeUnit.MILLISECONDS)
        delta = after - before
        _      <- IO(println(s"after : $after"))
        _      <- IO(println(s"delta : $delta"))
        _      <- IO { println(s"n=$n") }
        // sleep again to adjust
        _      <- Timer[IO].sleep((1000 - delta).millis)
        _      <- app(n - 1, delta)
      } yield ()

  override def run: IO[Unit] = app(10, 0)

}
