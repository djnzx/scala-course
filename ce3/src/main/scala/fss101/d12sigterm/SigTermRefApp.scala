package fss101.d12sigterm

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import cats.implicits.catsSyntaxEitherId
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object SigTermRefApp extends IOApp.Simple {

  val hook: IO[Deferred[IO, Unit]] = IO.deferred[Unit]

  def shutdownHook = hook
    .flatMap(_.complete(()))
    .unsafeRunSync()(cats.effect.unsafe.implicits.global)

  sys.addShutdownHook(shutdownHook)

  val haltSignal: IO[Either[Throwable, Unit]] =
    hook
      .flatMap(_.get)
      .map(_.asRight[Throwable])

  /** allocate cell */
  def mkRef: IO[Ref[IO, List[FiniteDuration]]] =
    IO.ref(List.empty[FiniteDuration])

  /** collect data to cell */
  def collect(d: FiniteDuration)(ref: Ref[IO, List[FiniteDuration]]): IO[Unit] =
    ref.update(d :: _)

  /** report all things collected */
  def report(ref: Ref[IO, List[FiniteDuration]]): IO[Unit] =
    ref.get.flatMap { ds =>
      IO.blocking {
        println("collected:")
        ds.foreach(x => pprint.log(x))
      }
    }

  def infiniteStream(ref: Ref[IO, List[FiniteDuration]]): fs2.Stream[IO, Nothing] =
    fs2.Stream
      .awakeEvery[IO](1.second)
      .evalTap(x => IO.println(x))
      .evalTap(x => collect(x)(ref))
      .drain

  override def run: IO[Unit] =
    mkRef
      .flatMap { ref =>

        val finalizer =
          IO.println("SIGTERM! handled") >> report(ref)

        infiniteStream(ref)
          .interruptWhen(haltSignal)
          .onFinalize(finalizer)
          .compile
          .drain
      }

}
