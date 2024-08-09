package fss101.d12sigterm

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxEitherId
import scala.concurrent.duration.DurationInt

object SigTermApp extends IOApp.Simple {

  val hook: IO[Deferred[IO, Unit]] = IO.deferred[Unit]

  def shutdownHook = hook
    .flatMap(_.complete(()))
    .unsafeRunSync()(cats.effect.unsafe.implicits.global)

  /** JVM platform-related */
  sys.addShutdownHook(shutdownHook)

  val haltSignal: IO[Either[Throwable, Unit]] =
    hook
      .flatMap(_.get)
      .map(_.asRight[Throwable])

  def infiniteStream = fs2.Stream
    .awakeEvery[IO](1.second)
    .evalTap(IO.println)
    .drain

  val finalizer: IO[Unit] =
    IO.println("SIGTERM! handled")

  override def run: IO[Unit] =
    infiniteStream
      .interruptWhen(haltSignal)
      .onFinalize(finalizer)
      .compile
      .drain

}
