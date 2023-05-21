package http4middle.accurate

import cats.Applicative
import cats.Functor
import cats.implicits.catsSyntaxApplicativeId
import cats.implicits.catsSyntaxEitherId
import cats.implicits.toFunctorOps
import org.http4s.Request
import scala.util.Try

object ContextExtractor {

  /** general alias to express context extraction */
  type Extractor[F[_], E, A] = Request[F] => F[Either[E, A]]

  private val unit: Unit = ()

  def either[F[_], E, A](f: Request[F] => F[Either[E, A]]): Extractor[F, E, A] = f

  def either[F[_]: Applicative, E, A](f: Request[F] => Either[E, A]): Extractor[F, E, A] = f.andThen(_.pure[F])

  def option[F[_]: Functor, A](f: Request[F] => F[Option[A]]): Extractor[F, Unit, A] = f.andThen(_.map(_.toRight(unit)))

  def option[F[_]: Applicative, A](f: Request[F] => Option[A]): Extractor[F, Unit, A] = either(f.andThen(_.toRight(unit)))

  def pure[F[_]: Applicative, A](f: Request[F] => A): Extractor[F, Nothing, A] = either(f.andThen(_.asRight))

  def unsafe[F[_]: Applicative, A](f: Request[F] => A): Extractor[F, Throwable, A] = either { rq: Request[F] => Try(f(rq)).toEither }

}
