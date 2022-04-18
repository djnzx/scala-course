package rtj_ce.part4coordination

import cats.effect._
import cats.effect.kernel.Outcome
import cats.syntax.traverse._
import scala.concurrent.duration._
import utils._

object C2DefersRacePair extends IOApp.Simple {

  /** Exercises:
    *   - (medium) write a small alarm notification with two simultaneous IOs
    *     - one that increments a counter every second (a clock)
    *     - one that waits for the counter to become 10, then prints a message "time's up!"
    *
    *   - (mega hard) implement racePair with Deferred.
    *     - use a Deferred which can hold an Either[outcome for ioa, outcome for iob]
    *     - start two fibers, one for each IO
    *     - on completion (with any status), each IO needs to complete that Deferred (hint: use a finalizer from the
    *       Resources lesson) (hint2: use a guarantee call to make sure the fibers complete the Deferred)
    *     - what do you do in case of cancellation (the hardest part)?
    */
  def eggBoiler(): IO[Unit] = {

    // background process
    def tickingClock(counter: Ref[IO, Int], signal: Deferred[IO, Unit]): IO[Unit] = for {
      _     <- IO.sleep(1.second)
      count <- counter.updateAndGet(_ + 1)
      _     <- IO(count).debug
      _     <- if (count >= 10) signal.complete(()) else tickingClock(counter, signal)
    } yield ()

    // waiter
    def eggReadyNotification(signal: Deferred[IO, Unit]) = for {
      _ <- IO("Egg boiling on some other fiber, waiting...").debug
      _ <- signal.get
      _ <- IO("EGG READY!").debug
    } yield ()

    // app
    for {
      counter         <- Ref[IO].of(0)
      signal          <- Deferred[IO, Unit]
      notificationFib <- eggReadyNotification(signal).start
      clock           <- tickingClock(counter, signal).start
      _               <- notificationFib.join
      _               <- clock.join
    } yield ()
  }

  type RaceResult[A, B] = Either[
    (Outcome[IO, Throwable, A], Fiber[IO, Throwable, B]), // (winner result, loser fiber)
    (Fiber[IO, Throwable, A], Outcome[IO, Throwable, B]), // (loser fiber, winner result)
  ]

  type EitherOutcome[A, B] = Either[Outcome[IO, Throwable, A], Outcome[IO, Throwable, B]]

  def ourRacePair[A, B](ioa: IO[A], iob: IO[B]): IO[RaceResult[A, B]] = IO.uncancelable { poll =>
    for {
      signal <- Deferred[IO, EitherOutcome[A, B]]
      fiba   <- ioa.guaranteeCase(outcomeA => signal.complete(Left(outcomeA)).void).start
      fibb   <- iob.guaranteeCase(outcomeB => signal.complete(Right(outcomeB)).void).start
      result <- poll(signal.get).onCancel { // blocking call - should be cancelable
                  for {
                    cancelFibA <- fiba.cancel.start
                    cancelFibB <- fibb.cancel.start
                    _          <- cancelFibA.join
                    _          <- cancelFibB.join
                  } yield ()
                }
    } yield result match {
      case Left(outcomeA)  => Left((outcomeA, fibb))
      case Right(outcomeB) => Right((fiba, outcomeB))
    }
  }

  override def run = eggBoiler()
}
