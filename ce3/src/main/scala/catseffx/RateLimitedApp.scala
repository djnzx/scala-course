package catseffx

import cats.effect._
import cats.effect.implicits._
import cats.effect.std.Semaphore
import cats.implicits._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object RateLimitedApp extends IOApp {

  def delayed[A, B](f: A => IO[B], duration: FiniteDuration): A => IO[B] = { a: A =>
    for {
      fiber <- IO.sleep(duration).start // how many seconds to sleep
      b     <- f(a)
      _     <- fiber.join
    } yield b
  }

  def wrappedWithLimitedRate[A, B](f: A => IO[B], sem: Semaphore[IO]): A => IO[B] = { a: A =>
    for {
      _          <- sem.acquire
      t: IO[Unit] = IO.sleep(1.second) // how many seconds to sleep
      fiber      <- t.start
      b          <- f(a)
      _          <- fiber.join
      _          <- sem.release
    } yield b
  }

  def sout(a: Int): IO[Unit] = IO(println(s"a=$a, ${Thread.currentThread().getName}"))

  val myData: List[Int] = 1 to 30 to List

  val limited: IO[List[Unit]] = for {
    sem    <- Semaphore[IO](5) // how many prints an one second
    delayed = wrappedWithLimitedRate(sout, sem)
//    limited = wrappedWithLimitedRate(sout, sem)
    xs     <- myData.traverse(delayed)
//    xs    <- myData.parTraverse(limited)
  } yield xs

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- limited
  } yield ExitCode.Success

}
