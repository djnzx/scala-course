package rtj_ce.part3concurrency

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Outcome
import cats.effect.kernel.Outcome.Canceled
import cats.effect.kernel.Outcome.Errored
import cats.effect.kernel.Outcome.Succeeded
import cats.syntax.apply._
import scala.concurrent.duration._
import utils._

object C1FibersExercises extends IOApp.Simple {

  case class Rex(msg: String) extends RuntimeException(msg)

  /** Exercises:
    *   1. Write a function that runs an IO on another thread, and, depending on the result of the fiber
    *      - return the result in an IO
    *      - if errored or cancelled, return a failed IO
    *
    * 2. Write a function that takes two IOs, runs them on different fibers and returns an IO with a tuple containing
    * both results.
    *   - if both IOs complete successfully, tuple their results
    *   - if the first IO returns an error, raise that error (ignoring the second IO's result/error)
    *   - if the first IO doesn't error but second IO returns an error, raise that error
    *   - if one (or both) canceled, raise a RuntimeException
    *
    * 3. Write a function that adds a timeout to an IO:
    *   - IO runs on a fiber
    *   - if the timeout duration passes, then the fiber is canceled
    *   - the method returns an IO[A] which contains
    *     - the original value if the computation is successful before the timeout signal
    *     - the exception if the computation is failed before the timeout signal
    *     - a RuntimeException if it times out (i.e. cancelled by the timeout)
    */
  // 1
  def processResultsFromFiber1[A](io: IO[A]): IO[A] = {

    val x: IO[Outcome[IO, Throwable, A]] = for {
      f <- io.start
      r <- f.join
    } yield r

    val r: IO[A] = x.flatMap {
      case Succeeded(a) => a
      case Errored(x)   => IO.raiseError(x)
      case Canceled()   => IO.raiseError(Rex("Cancelled"))
    }

    r
  }

  def processResultsFromFiber[A](io: IO[A]): IO[A] = {
    val ioResult = for {
      fib    <- io.debug0.start
      result <- fib.join
    } yield result

    ioResult.flatMap {
      case Succeeded(fa) => fa
      case Errored(e)    => IO.raiseError(e)
      case Canceled()    => IO.raiseError(new RuntimeException("Computation canceled."))
    }
  }

  def testEx1() = {
    val aComputation = IO("starting").debug0 >> IO.sleep(1.second) >> IO("done!").debug0 >> IO(42)
    processResultsFromFiber(aComputation).void
  }

  // 2
  def tupleIOs[A, B](ioa: IO[A], iob: IO[B]): IO[(A, B)] = {
    val result: IO[(Outcome[IO, Throwable, A], Outcome[IO, Throwable, B])] = for {
      // spawn them to the separate fibers
      fiba    <- ioa.start
      fibb    <- iob.start
      // just collect results
      resulta <- fiba.join
      resultb <- fibb.join
    } yield (resulta, resultb)

    val r: IO[(A, B)] = result.flatMap {
      case (Succeeded(fa), Succeeded(fb)) => (fa, fb).tupled
      case (Errored(e1), _)               => IO.raiseError(e1)
      case (_, Errored(e2))               => IO.raiseError(e2)
      case _                              => IO.raiseError(new RuntimeException("Some computation canceled."))
    }

    r
  }

  def testEx2() = {
    val firstIO = IO.sleep(2.seconds) >> IO(1).debug0
    val secondIO = IO.sleep(3.seconds) >> IO(2).debug0
    tupleIOs(firstIO, secondIO).debug0.void
  }

  // 3
  def timeout[A](io: IO[A], duration: FiniteDuration): IO[A] = {
    val computation = for {
      fib    <- io.start
//      _ <- (IO.sleep(duration) >> fib.cancel).start // careful - fibers can leak. resources need to be released
      _      <- IO.sleep(duration) >> fib.cancel
      result <- fib.join
    } yield result

    computation.flatMap {
      case Succeeded(fa) => fa
      case Errored(e)    => IO.raiseError(e)
      case Canceled()    => IO.raiseError(new RuntimeException("Computation canceled."))
    }
  }

  def testEx3() = {
    val aComputation = IO("starting").debug0 >> IO.sleep(1.second) >> IO("done!").debug0 >> IO(42)
    timeout(aComputation, 500.millis).debug0.void
  }

  override def run = processResultsFromFiber(IO(3.6)).void
}
