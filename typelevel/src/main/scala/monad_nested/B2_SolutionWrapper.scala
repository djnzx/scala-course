package monad_nested

import cats.{Functor, Monad}
import cats.effect.IO

object B2_SolutionWrapper extends App {

  case class OptWrapper[F[_], A](value: F[Option[A]]) {
    def map[B](f: A => B)(implicit F: Functor[F]): OptWrapper[F, B] = OptWrapper(
      F.map(value) { oa: Option[A] =>
        oa.map(f)
      }
    )
    def flatMap[B](f: A => OptWrapper[F, B])(implicit F: Monad[F]): OptWrapper[F, B] = OptWrapper(
      F.flatMap(value) { oa: Option[A] => oa match {
        case Some(a) => f(a).value
        case None    => F.pure(Option.empty[B])
      }}
    )
  }

  val ot5: OptWrapper[IO, Int] = OptWrapper(ioo5)
  val ot6: OptWrapper[IO, Int] = OptWrapper(ioo6)

  val sumT = for {
    v5 <- ot5
    v6 <- ot6
  } yield v5 + v6

  val sum: IO[Option[Int]] = sumT.value
  println(sum.unsafeRunSync())

}
