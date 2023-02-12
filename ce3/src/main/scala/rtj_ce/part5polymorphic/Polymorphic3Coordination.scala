package rtj_ce.part5polymorphic

import cats.effect.Concurrent
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.MonadCancel
import cats.effect.Ref
import cats.effect.kernel.Deferred
import cats.effect.kernel.Spawn
import utils._

object Polymorphic3Coordination extends IOApp.Simple {

  // Concurrent - Ref + Deferred for ANY effect type
  trait MyConcurrent[F[_]] extends Spawn[F] {
    def ref[A](a: A): F[Ref[F, A]]
    def deferred[A]: F[Deferred[F, A]]
  }

  val concurrentIO = Concurrent[IO] // given instance of Concurrent[IO]
  val aDeferred = Deferred[IO, Int] // given/implicit Concurrent[IO] in scope
  val aDeferred_v2 = concurrentIO.deferred[Int]
  val aRef = concurrentIO.ref(42)

  // capabilities: pure, map/flatMap, raiseError, uncancelable, start (fibers), + ref/deferred

  import scala.concurrent.duration._

  def eggBoiler(): IO[Unit] = {
    def eggReadyNotification(signal: Deferred[IO, Unit]) = for {
      _ <- IO("Egg boiling on some other fiber, waiting...").debug0
      _ <- signal.get
      _ <- IO("EGG READY!").debug0
    } yield ()

    def tickingClock(counter: Ref[IO, Int], signal: Deferred[IO, Unit]): IO[Unit] = for {
      _     <- IO.sleep(1.second)
      count <- counter.updateAndGet(_ + 1)
      _     <- IO(count).debug0
      _     <- if (count >= 10) signal.complete(()) else tickingClock(counter, signal)
    } yield ()

    for {
      counter         <- Ref[IO].of(0)
      signal          <- Deferred[IO, Unit]
      notificationFib <- eggReadyNotification(signal).start
      clock           <- tickingClock(counter, signal).start
      _               <- notificationFib.join
      _               <- clock.join
    } yield ()
  }

  import cats.effect.syntax.spawn._ // start extension method
  import cats.syntax.flatMap._ // flatMap
  import cats.syntax.functor._ // map

  // added here explicitly due to a Scala 3 bug that we discovered during lesson recording
  def unsafeSleepDupe[F[_], E](duration: FiniteDuration)(implicit mc: MonadCancel[F, E]): F[Unit] =
    mc.pure(Thread.sleep(duration.toMillis))

  def polymorphicEggBoiler[F[_]](implicit concurrent: Concurrent[F]): F[Unit] = {
    def eggReadyNotification(signal: Deferred[F, Unit]) = for {
      _ <- concurrent.pure("Egg boiling on some other fiber, waiting...").debug0
      _ <- signal.get
      _ <- concurrent.pure("EGG READY!").debug0
    } yield ()

    def tickingClock(counter: Ref[F, Int], signal: Deferred[F, Unit]): F[Unit] = for {
      _     <- unsafeSleepDupe[F, Throwable](1.second)
      count <- counter.updateAndGet(_ + 1)
      _     <- concurrent.pure(count).debug0
      _     <- if (count >= 10) signal.complete(()).void else tickingClock(counter, signal)
    } yield ()

    for {
      counter         <- concurrent.ref(0)
      signal          <- concurrent.deferred[Unit]
      notificationFib <- eggReadyNotification(signal).start
      clock           <- tickingClock(counter, signal).start
      _               <- notificationFib.join
      _               <- clock.join
    } yield ()
  }

  override def run = polymorphicEggBoiler[IO]
}
