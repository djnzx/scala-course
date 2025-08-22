package timed

import cats.effect.IO
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt
import threads.Tools

class TimedExplore extends CatsEffectSuite with Matchers with Tools {

  test("5s+5s") {
    (IO.sleep(5.second) >> IO.println("1")).timed
      .map { case (t, _) => t.toMicros }
      .flatTap(x => IO.println(x))
      .flatMap(t => IO(t should be >= 5_000_000L)) >>
      (IO.sleep(5.second) >> IO.println("2")).timed
        .map { case (t, _) => t.toMicros }
        .flatTap(x => IO.println(x))
        .flatMap(t => IO(t should be >= 5_000_000L))
  }

  test("1ms+1ms") {
    IO.sleep(5.second) >> IO.println("1").timed
      .map { case (t, _) => t.toMicros }
      .flatTap(x => IO.println(x))
      .flatMap(t => IO(t should be < 1_000L)) >>
      IO.sleep(5.second) >> IO.println("2").timed
        .map { case (t, _) => t.toMicros }
        .flatTap(x => IO.println(x))
  }

  test("5s,10s") {
    IO.sleep(5.second)
      .flatMap(_ => IO.println("1"))
      .timed
      .map { case (t, _) => t.toMicros }
      .flatTap(x => IO.println(x))
      .flatMap(t => IO(t should be >= 5_000_000L))
      .flatMap(_ => IO.sleep(5.second))
      .flatMap(_ => IO.println("2"))
      .timed
      .map { case (t, _) => t.toMicros }
      .flatTap(x => IO.println(x))
      .flatMap(t => IO(t should be >= 10_000_000L))
  }

}
