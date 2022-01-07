package rtj.part3concurrency

import cats.effect.kernel.Outcome.Canceled
import cats.effect.kernel.Outcome.Errored
import cats.effect.kernel.Outcome.Succeeded
import cats.effect.Fiber
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Outcome
import scala.concurrent.duration._

object C1Fibers extends IOApp.Simple {

  val meaningOfLife = IO.pure(42)
  val favLang = IO.pure("Scala")

  import utils._

  def sameThreadIOs() = for {
    _ <- meaningOfLife.debug
    _ <- favLang.debug
  } yield ()

  // introducing Fiber: a data structure describing an effect running on some thread
  def createFiber: Fiber[IO, Throwable, String] = ??? // almost impossible to create fibers manually

  // the fiber is not actually started, but the fiber allocation is wrapped in another effect
  val aFiber: IO[Fiber[IO, Throwable, Int]] = meaningOfLife.debug.start

  def differentThreadIOs() = for {
    _ <- aFiber
    _ <- favLang.debug
  } yield ()

  // joining a fiber
  def runOnSomeOtherThread[A](io: IO[A]): IO[Outcome[IO, Throwable, A]] = for {
    fib <- io.start
    result <- fib.join // an effect which waits for the fiber to terminate
  } yield result
  /*
    possible outcomes:
    - success with an IO
    - failure with an exception
    - cancelled
   */

  val someIOOnAnotherThread = runOnSomeOtherThread(meaningOfLife)
  val someResultFromAnotherThread = someIOOnAnotherThread.flatMap {
    case Succeeded(effect) => effect
    case Errored(e @ _)    => IO(0)
    case Canceled()        => IO(0)
  }

  def throwOnAnotherThread() = for {
    fib <- IO.raiseError[Int](new RuntimeException("no number for you")).start
    result <- fib.join
  } yield result

  def testCancel() = {
    val task: IO[String] = IO("starting").debug >> IO.sleep(1.second) >> IO("done").debug
    // onCancel is a "finalizer", allowing you to free up resources in case you get canceled
    val taskWithCancellationHandler: IO[String] = task.onCancel(IO("I'm being cancelled!").debug.void)

    for {
      fib <- taskWithCancellationHandler.start // on a separate thread
      _ <- IO.sleep(500.millis) >> IO("cancelling").debug // running on the calling thread
      _ <- fib.cancel
      result <- fib.join
    } yield result
  }

  override def run = testCancel().void
}
