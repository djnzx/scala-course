package catsasync

import cats.effect.IO
import cats.effect.kernel.Outcome
import cats.implicits._
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import scala.concurrent.duration.DurationInt

class IoAsyncExplore extends CatsEffectSuite with Matchers {

  val lf: LoggerFactory[IO] = Slf4jFactory.create[IO]
  val log = lf.getLogger

  implicit class DebugOps[A](fa: IO[A]) {
    def tt(m: String): IO[A] =
      log.info(m) >> fa
  }

  def fetchData(): Int = {
    println("sleeping 1s  [%21s]".formatted(Thread.currentThread().getName))
    Thread.sleep(1000)
    42
  }

  def fetchDataAsync(callback: Either[Throwable, Int] => Unit): Unit =
    new Thread {
      val x: Either[Throwable, Int] = Either.catchNonFatal(fetchData())
      callback(x)
    }.start()

  val io: IO[Int] = IO.async { cb =>
    IO.blocking {
      fetchDataAsync(cb)
      None
    }
  }

  val onCancelFinalizer: IO[Unit] = log.info("was cancelled")

  val ioFin: IO[Int] = IO.async { cb =>
    IO {
      fetchDataAsync(cb)
      Some(onCancelFinalizer)
    }
  }

  test("without cancellation finalizer") {
    for {
      _ <- log.info("started")
      f <- io.tt("fork").start
      x <- f.join.tt("joined semantically")
      _ <- x match {
             case Outcome.Succeeded(fa) => fa.flatMap(a => log.info(s"actually got result: $a"))
             case Outcome.Errored(e)    => log.info(s"failed $e")
             case Outcome.Canceled()    => log.info("cancelled")
           }
      _ <- log.info("finished")
    } yield ()
  }

  test("with cancellation finalizer") {
    for {
      _ <- log.info("started")
      f <- ioFin.tt("forked").start
      _ <- IO.sleep(500.millis) >> f.cancel // cancelled after 500ms, then finalizer, then `Outcome.Canceled`
      x <- f.join.tt("joined semantically")
      _ <- x match {
             case Outcome.Succeeded(fa) => fa.flatMap(a => log.info(s"actually got result: $a"))
             case Outcome.Errored(e)    => log.info(s"failed $e")
             case Outcome.Canceled()    => log.info("cancelled")
           }
      _ <- log.info("finished")
    } yield ()

  }

}
