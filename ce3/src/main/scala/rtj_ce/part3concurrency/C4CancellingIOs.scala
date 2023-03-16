package rtj_ce.part3concurrency

import cats.effect.IO
import cats.effect.IOApp
import scala.concurrent.duration._
import utils._

object C4CancellingIOs extends IOApp.Simple {

  /*
    Cancelling IOs
    - fib.cancel
    - IO.race & other APIs
    - manual cancellation
   */
  val chainOfIOs: IO[Int] = IO("waiting").debug0 >> IO.canceled >> IO(42).debug0

  // uncancelable
  // example: online store, payment processor
  // payment process must NOT be canceled
  val specialPaymentSystem = (
    IO("Payment running, don't cancel me...").debug0 >>
      IO.sleep(1.second) >>
      IO("Payment completed.").debug0
  ).onCancel(IO("MEGA CANCEL OF DOOM!").debug0.void)

  val cancellationOfDoom = for {
    fib <- specialPaymentSystem.start
    _   <- IO.sleep(500.millis) >> fib.cancel
    _   <- fib.join
  } yield ()

  val atomicPayment: IO[String] = IO.uncancelable(_ => specialPaymentSystem) // "masking"
  val atomicPayment_v2 = specialPaymentSystem.uncancelable // same

  val noCancellationOfDoom = for {
    fib <- atomicPayment.start
    _   <- IO.sleep(500.millis) >> IO("attempting cancellation...").debug0 >> fib.cancel
    _   <- fib.join
  } yield ()

  /*
    The uncancelable API is more complex and more general.
    It takes a function from Poll[IO] to IO. In the example above, we aren't using that Poll instance.
    The Poll object can be used to mark sections within the returned effect which CAN BE CANCELED.
   */

  /*
    Example: authentication service. Has two parts:
    - input password, can be cancelled, because otherwise we might block indefinitely on user input
    - verify password, CANNOT be cancelled once it's started
   */
  val inputPassword =
    IO("Input password:").debug0 >> IO("(typing password)").debug0 >> IO.sleep(2.seconds) >> IO("RockTheJVM1!")
  val verifyPassword = (pw: String) => IO("verifying...").debug0 >> IO.sleep(2.seconds) >> IO(pw == "RockTheJVM1!")

  val authFlow: IO[Unit] = IO.uncancelable { poll =>
    for {
      pw       <- poll(inputPassword).onCancel(
                    IO("Authentication timed out. Try again later.").debug0.void
                  ) // this is cancelable
      verified <- verifyPassword(pw) // this is NOT cancelable
      _        <-
        if (verified) IO("Authentication successful.").debug0 // this is NOT cancelable
        else IO("Authentication failed.").debug0
    } yield ()
  }

  val authProgram = for {
    authFib <- authFlow.start
    _       <- IO.sleep(3.seconds) >> IO("Authentication timeout, attempting cancel...").debug0 >> authFib.cancel
    _       <- authFib.join
  } yield ()

  /*
    Uncancelable calls are MASKS which suppress cancellation.
    Poll calls are "gaps opened" in the uncancelable region.
   */

  /** Exercises: what do you think the following effects will do?
    *   1. Anticipate 2. Run to see if you're correct 3. Prove your theory
    */
  // 1
  val cancelBeforeMol = IO.canceled >> IO(42).debug0
  val uncancelableMol = IO.uncancelable(_ => IO.canceled >> IO(42).debug0)
  // uncancelable will eliminate ALL cancel points

  // 2
  val invincibleAuthProgram = for {
    authFib <- IO.uncancelable(_ => authFlow).start
    _       <- IO.sleep(1.seconds) >> IO("Authentication timeout, attempting cancel...").debug0 >> authFib.cancel
    _       <- authFib.join
  } yield ()
  /*
    Lesson: Uncancelable calls are masks which suppress all existing cancelable gaps (including from a previous uncancelable).
   */

  // 3
  def threeStepProgram(): IO[Unit] = {
    val sequence = IO.uncancelable { poll =>
      poll(IO("cancelable").debug0 >> IO.sleep(1.second) >> IO("cancelable end").debug0) >>
        IO("uncancelable").debug0 >> IO.sleep(1.second) >> IO("uncancelable end").debug0 >>
        poll(IO("second cancelable").debug0 >> IO.sleep(1.second) >> IO("second cancelable end").debug0)
    }

    for {
      fib <- sequence.start
      _   <- IO.sleep(1500.millis) >> IO("CANCELING").debug0 >> fib.cancel
      _   <- fib.join
    } yield ()
  }
  /*
    Lesson: Uncancelable regions ignore cancellation signals, but that doesn't mean the next CANCELABLE region won't take them.
   */

  override def run = threeStepProgram()
}
