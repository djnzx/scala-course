package fs2x

import cats.effect.concurrent.Deferred
import cats.effect.{ContextShift, IO, Timer}
import fs2.Stream

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Fs2_07Interrupt extends App {
  /**
    *          Concurrent
    *              |
    *            Async
    *           /   \
    *        Sync   LiftIO
    *     /      \
    * Bracket   Defer
    *    |
    * MonadError
    */
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  val wholeProgram: Stream[IO, Unit] =
    Stream.eval(Deferred[IO, Unit]).flatMap { switch =>

      val printNow: IO[Unit] = IO(println(java.time.LocalTime.now))
      val terminate: IO[Unit] = switch.complete(())

      val switcher: Stream[IO, Unit] =
        Stream.eval(terminate).delayBy(5.seconds)

      val main: Stream[IO, Unit] =
        Stream.repeatEval(printNow).metered(1.second)

      main
        .interruptWhen(switch.get.attempt)
        .concurrently(switcher)
    }

  wholeProgram.compile.drain.unsafeRunSync
}
