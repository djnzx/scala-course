package fss101.d12sigterm

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Resource.ExitCase
import cats.implicits._
import scala.concurrent.duration.DurationInt

object StreamTerminationPlayground extends IOApp.Simple {

  val latch: IO[Deferred[IO, Either[Throwable, Unit]]] = IO.deferred[Either[Throwable, Unit]]

  def hook = latch
    .flatTap(_ => IO.println("2. Terminating JVM (maybe due to SIGINT/SIGTERM)"))
    .flatMap(d => d.complete(().asRight)) // success termination ???
//    .flatMap(_.complete(new IllegalArgumentException("boom!!!").asLeft)) // failed termination ???
    .flatTap(_ => IO.println("3. Deferred completed"))
    .unsafeRunSync()(cats.effect.unsafe.implicits.global)

  /** JVM platform-related */
  sys.addShutdownHook(hook)

  val haltSignal: IO[Either[Throwable, Unit]] =
    latch
      .flatTap(_ => IO.println("1. Waiting for Deferred to be completed"))
      .flatMap(_.get)

  def infiniteStream = fs2.Stream
    .awakeEvery[IO](1.second)
//    .take(4)
    .evalTap(IO.println)
    .drain

  /*
    we can interrupt ???
      - with error    - Left(Throwable)
      - without error - Right(Unit)

    // 1a. based on the F[Either[E, Unit]]
    def interruptWhen(haltOnSignal: F[Either[Throwable, Unit]]          ): Stream[F, O]

    // 1b. based on the Deferred resolved to F[Either[E, Unit]]
    def interruptWhen(haltWhenTrue: Deferred[F, Either[Throwable, Unit]]): Stream[F, O]

    // 2a. based on Stream contains "true"
    def interruptWhen(haltWhenTrue: Stream[F, Boolean]                  ): Stream[F, O]

    // 2b. based on Signal contains "true"
    def interruptWhen(haltWhenTrue: Signal[F2, Boolean]                 ): Stream[F, O]
   */
  override def run: IO[Unit] =
    infiniteStream
      .timeout(5.second)                    // ExitCase.Errored
      .interruptWhen(haltSignal)
      .onFinalizeCase {
        case ExitCase.Succeeded  => IO.println("Succeeded - Just finished") // Just finished
        case ExitCase.Errored(e) => IO.println(s"Errored - With exception (any unhandled): $e")
        case ExitCase.Canceled   => IO.println("4. Canceled - via .interruptWhen()")
      }
      .onFinalize(IO.println("9. finally")) // in the end of the stream regardless the case
      .compile
      .drain

}
