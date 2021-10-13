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

  val ph: Par[Unit] = Parallel[IO].parallel(hello)
  val pw: Par[Unit] = Parallel[IO].parallel(world)

  /** parallel doesn't have a monad, so, can't be sequenced */
//  val hw1 = for {
//    _ <- ph
//    _ <- pw
//  } yield ()

  /** still sequential because for monads, map2 is based on flatMap */
  val par_hw: Par[Unit] =
    (ph, pw)
      .mapN((_, _) => ())

  val hw: IO[Unit] = Parallel[IO].sequential(par_hw)
  hw.unsafeRunSync()

  // or
  val r: IO[Unit] = (hello, world).parMapN((_, _) => ())

  r.unsafeRunSync()

}
