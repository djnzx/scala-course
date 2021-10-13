package d01

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxTuple2Semigroupal

object IOComposition extends App {

  def hello = IO(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] Hello"))
  def world = IO(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] World"))

  /** sequential, can depend on previous */
  val hw1: IO[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()

  /** still sequential because for monads, map2 is based on flatMap */
  val hw2: IO[Unit] =
    (hello, world)
      .mapN((_, _) => ())

  hw1.unsafeRunSync()
  hw2.unsafeRunSync()

}
