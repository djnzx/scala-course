package catseffx

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Timer
import cats.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object DelayedApp extends IOApp {

  def withDelay[A, B](f: A => IO[B], duration: FiniteDuration): A => IO[B] = { a: A =>
    for {
      fiber <- Timer[IO].sleep(duration) //.start
      t0 <- IO.pure(t())
      b <- f(a)
      _ <- IO { pdt(t0) }
//      _    <- fiber.join
    } yield b
  }

  def body(a: Int): IO[Unit] = IO { println(s"a=$a, ${Thread.currentThread().getName}") }

  val source: List[Int] = 1 to 30 to List
  val traverseNoDelay: IO[List[Unit]] = source.traverse(body)
  val traverseDelayed: IO[List[Unit]] = source.traverse(withDelay(body, 1.second))
  // doesn't work
  val traverseDelayedP: IO[List[Unit]] = source.parTraverse(withDelay(body, 1.second))

  override def run(args: List[String]): IO[ExitCode] = for {
//    _ <- traverseDelayedP
    t0 <- IO.pure(t())
    _ <- Timer[IO].sleep(2 seconds)
    _ <- pdtIO(t0)
  } yield ExitCode.Success

}

object ABC {
  IO.delay()
  IO.raiseError(new IllegalArgumentException)
}
