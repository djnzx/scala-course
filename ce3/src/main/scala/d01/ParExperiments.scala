package d01

import cats.Parallel
import cats.effect.IO
import cats.effect.IO.Par
import cats.effect.implicits.commutativeApplicativeForParallelF
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxTuple2Parallel
import cats.implicits.catsSyntaxTuple2Semigroupal

object ParExperiments extends App {

  val hello: IO[Unit] = IO(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] Hello"))
  val world: IO[Unit] = IO(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] World"))

  val combine = (_: Unit, _: Unit) => ()

  /** make them Parallel */
  val ph: Par[Unit] = Parallel[IO].parallel(hello)
  val pw: Par[Unit] = Parallel[IO].parallel(world)

  /** parallel doesn't have a monad, so, can't be sequenced */
//  val hw1 = for {
//    _ <- ph
//    _ <- pw
//  } yield ()

  /** Parallel does have Applicative, so we can use mapN */
  val par_hw: Par[Unit] =
    (ph, pw).mapN(combine)

  /** make them Sequential back */
  val r1: IO[Unit] = Parallel[IO].sequential(par_hw)
  r1.unsafeRunSync()

  /** or do everything in one shot */
  val r2: IO[Unit] = (hello, world).parMapN(combine)
  r2.unsafeRunSync()

}
