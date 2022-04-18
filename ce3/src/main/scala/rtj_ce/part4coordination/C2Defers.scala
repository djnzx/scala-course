package rtj_ce.part4coordination

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.IOApp
import scala.concurrent.duration._
import utils._

/**   - Deferred is a functional primitive which is
  *   - like promise, maybe, will be resolved, only once
  */
object C2Defers extends IOApp.Simple {

  val deferred1: IO[Deferred[IO, Int]] = Deferred[IO, Int]
  val deferred2: IO[Deferred[IO, Int]] = IO.deferred[Int] // the same

  // get blocks the calling fiber (semantically) until some other fiber completes the Deferred with a value
  val reader: IO[Int] = deferred1.flatMap { signal: Deferred[IO, Int] =>
    signal.get // blocks the fiber
  }

  val writer: IO[Boolean] = deferred1.flatMap { signal =>
    signal.complete(42)
  }

  def demoDeferred(): IO[Unit] = {

    def consumer(signal: Deferred[IO, Int]) = for {
      _             <- IO("[consumer] waiting for result...").debug
      meaningOfLife <- signal.get // blocker
      _             <- IO(s"[consumer] got the result: $meaningOfLife").debug
    } yield ()

    def producer(signal: Deferred[IO, Int]) = for {
      _             <- IO("[producer] crunching numbers...").debug
      _             <- IO.sleep(1.second)
      _             <- IO("[producer] complete: 42").debug
      meaningOfLife <- IO(42)
      _             <- signal.complete(meaningOfLife)
    } yield ()

    // app
    for {
      signal      <- Deferred[IO, Int]
      fibConsumer <- consumer(signal).start
      fibProducer <- producer(signal).start
      _           <- fibProducer.join
      _           <- fibConsumer.join
    } yield ()
  }

  override def run = demoDeferred()
}
