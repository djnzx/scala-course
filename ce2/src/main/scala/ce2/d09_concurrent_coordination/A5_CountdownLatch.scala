package ce2.d09_concurrent_coordination

import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import ce2.common.debug.DebugHelper

trait CountdownLatch {

  /** block until counter is zero */
  def await: IO[Unit]

  /** atomically decrement encapsulated value */
  def decrement: IO[Unit]
}

object CountdownLatch {

  def apply(n: Long)(implicit cs: ContextShift[IO]): IO[CountdownLatch] =
    for {
      whenDone <- Deferred[IO, Unit]
      state <- Ref[IO].of[LatchState](KeepWaiting(n, whenDone))

      latch = new CountdownLatch {

        def await: IO[Unit] =
          state.get.flatMap {
            /** we are ready at the request moment */
            case Done => IO.unit
            /** block and wait */
            case KeepWaiting(_, whenDone) => whenDone.get
          }

        def decrement: IO[Unit] =
          state
            .modify {
              case KeepWaiting(1, whenDone) => Done -> whenDone.complete(())
              case KeepWaiting(n, whenDone) => KeepWaiting(n - 1, whenDone) -> IO.unit
              case Done                     => Done -> IO.unit
            }
            .flatten
            .uncancelable
      }
    } yield latch

  sealed trait LatchState

  /** keep waiting */
  case class KeepWaiting(n: Long, whenDone: Deferred[IO, Unit]) extends LatchState

  /** ready to release */
  case object Done extends LatchState
}

object LatchExample extends IOApp {

  def runPrerequisite(latch: CountdownLatch) =
    for {
      result <- IO("prerequisite").debug
      _ <- latch.decrement // <1>
    } yield result

  def actionRequiringPrerequisites(latch: CountdownLatch) =
    for {
      _ <- IO("waiting for prerequisites").debug
      _ <- latch.await // <1>
      result <- IO("action").debug // <2>
    } yield result

  def run(args: List[String]): IO[ExitCode] =
    for {
      latch <- CountdownLatch(1)
      _ <- (actionRequiringPrerequisites(latch), runPrerequisite(latch)).parTupled
    } yield ExitCode.Success

}
