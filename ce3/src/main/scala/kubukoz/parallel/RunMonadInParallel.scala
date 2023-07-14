package kubukoz.parallel

import cats.Applicative
import cats.NonEmptyParallel
import cats.Semigroup
import cats.effect.IO
import cats.implicits.catsSyntaxNonEmptyParallelAp
import cats.syntax.semigroup._

object RunMonadInParallel extends App {

  /** we can't use flatMap */
  def combine[E: Semigroup, A, B](
      l: Either[E, A],
      r: Either[E, B],
    ): Either[E, (A, B)] = (l, r) match {
    case (Left(e1), Left(e2)) => Left(e1 |+| e2)
    case (Left(e), _)         => Left(e)
    case (_, Left(e))         => Left(e)
    case (Right(a), Right(b)) => Right(a, b)
  }

  /** combine list */
  def combine[E: Semigroup, A](ee: List[Either[E, A]]): Either[E, List[A]] =
    ee.foldRight[Either[E, List[A]]](Right(Nil)) { combine(_, _).map { case (curr, prev) => curr :: prev } }

  /** Validated isn't a Monad, but Applicative + Semigroup for the Error */

  /** IO */
  def concurrently[A, B](l: IO[A], r: IO[B])(implicit p: NonEmptyParallel[IO]): IO[(A, B)] =
    l parProduct r

  class ParIO[A](a: IO[A])
  implicit val applicative: Applicative[ParIO] = ???

}
