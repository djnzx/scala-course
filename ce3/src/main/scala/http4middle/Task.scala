package http4middle

import cats._
import cats.data.NonEmptyList
import cats.data.Validated
import cats.data.ValidatedNel
import cats.implicits._

object Task extends App {

  def task1[F[_]: Monad, A, E](fs: List[A => F[Either[E, Unit]]]): A => F[Either[NonEmptyList[E], Unit]] =
    a =>
      fs
        .traverse(_(a))
        .map(_.foldMap(_.toValidatedNel).toEither)

  // hint 1
  val e1: Either[String, Int] = ???
  val v2: Validated[NonEmptyList[String], Int] = e1.toValidatedNel
  val v1: ValidatedNel[String, Int] = e1.toValidatedNel
  val e2: Either[NonEmptyList[String], Int] = v1.toEither

  // hint 2
  trait AnyCollection[F[_]] {
    def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B
    def foldMap[A, B](fa: F[A])(f: A => B)(implicit B: Monoid[B]): B =
      foldLeft(fa, B.empty)((b, a) => B.combine(b, f(a)))
  }

}
