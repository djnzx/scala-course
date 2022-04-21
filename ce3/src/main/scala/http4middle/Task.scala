package http4middle

import cats.Monad
import cats.data.NonEmptyList
import cats.implicits._

object Task extends App {

  def task1[F[_]: Monad, A, E](fs: List[A => F[Either[E, Unit]]]): A => F[Either[NonEmptyList[E], Unit]] =
    a =>
      fs
        .traverse(_(a))
        .map(_.foldMap(_.toValidatedNel).toEither)

}
