package http4middle

import cats.implicits.catsSyntaxApplicativeId
import cats.implicits.toFunctorOps
import cats.Applicative
import cats.Functor

/** strictly saying, any implementation is a function: A => F[B] can be: A => Id[B] A => IO[B] A => Future[B] A =>
  * ZIO[R, E, B] ...
  */
class ServiceImplementation[F[_]: Functor: Applicative] {

  /** core functions */
  def core(name: String): F[String] = s"Hello, $name.".pure[F]
  def core2(name: String): F[Student] = core(name).map(n => Student(33, name, n))
  def twice(x: Int): F[Int] = (x * 2).pure[F]

}
