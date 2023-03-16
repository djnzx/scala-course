package rtj_ce.part5polymorphic

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.MonadCancel
import cats.effect.implicits._
import utils.DebugWrapper

object Polymorphic1CancellationEx extends IOApp.Simple {

  /** Exercise - generalize a piece of code (the auth-flow example from the Cancellation lesson) */

  import cats.syntax.flatMap._
  import cats.syntax.functor._
  import scala.concurrent.duration._

  // hint: use this instead of IO.sleep
  def unsafeSleep[F[_], E](duration: FiniteDuration)(implicit mc: MonadCancel[F, E]): F[Unit] =
    mc.pure(Thread.sleep(duration.toMillis))

  def inputPassword[F[_], E](implicit mc: MonadCancel[F, E]): F[String] = for {
    _  <- mc.pure("Input password:").debug0
    _  <- mc.pure("(typing password)").debug0
    _  <- unsafeSleep[F, E](5.seconds)
    pw <- mc.pure("RockTheJVM1!")
  } yield pw

  def verifyPassword[F[_], E](pw: String)(implicit mc: MonadCancel[F, E]): F[Boolean] = for {
    _       <- mc.pure("verifying...").debug0
    _       <- unsafeSleep[F, E](2.seconds)
    checked <- mc.pure(pw == "RockTheJVM1!")
  } yield checked

  def authFlow[F[_], E](implicit mc: MonadCancel[F, E]): F[Unit] = mc.uncancelable { poll =>
    for {
      // this is cancelable
      pw       <- poll(inputPassword).onCancel(
                    mc.pure("Authentication timed out. Try again later.").debug0.void
                  )
      // this is NOT cancelable
      verified <- verifyPassword(pw)
      _        <-
        // this is NOT cancelable
        if (verified) mc.pure("Authentication successful.").debug0
        else mc.pure("Authentication failed.").debug0
    } yield ()
  }

  val authProgram: IO[Unit] = for {
    authFib <- authFlow[IO, Throwable].start
    _       <- IO.sleep(3.seconds) >> IO("Authentication timeout, attempting cancel...").debug0 >> authFib.cancel
    _       <- authFib.join
  } yield ()

  override def run = authProgram
}
