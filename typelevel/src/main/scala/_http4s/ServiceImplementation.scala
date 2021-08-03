package _http4s

import _http4s.Domain.Student
import cats.{Applicative, Functor}
import cats.implicits.{catsSyntaxApplicativeId, toFunctorOps}

/**
  * strictly saying,
  * any implementation is a
  * function: A => F[B]
  * can be:
  * A => Id[B]
  * A => IO[B]
  * A => Future[B]
  * A => ZIO[R, E, B]
  * ...
  */
class ServiceImplementation[F[_]: Functor: Applicative] {

  /** core functions */
  def core(name: String): F[String] = s"Hello, $name.".pure[F]
  def core2(name: String): F[Student] = core(name).map(n => Student(33, name, n))

}
