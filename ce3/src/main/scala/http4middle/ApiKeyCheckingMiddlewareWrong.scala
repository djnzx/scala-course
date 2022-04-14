package http4middle

import cats.data.Kleisli
import cats.data.OptionT
import cats.implicits._
import cats.Applicative
import cats.Id
import cats.Monad
import cats.~>
import io.circe.Encoder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.implicits._
import org.http4s.server.AuthMiddleware.defaultAuthFailure
import org.http4s.server.HttpMiddleware
import org.http4s.Request
import org.http4s.Response

object ApiKeyCheckingMiddlewareWrong {

  private val keyHeader = "ApiKey".ci
  private val keyMissing = s"Request should contain API key in the header $keyHeader"
  private val keyDoesntMatch = "API doesn't match"

  private def forbidden[F[_]: Monad, A: Encoder](a: A): Kleisli[OptionT[F, *], Request[F], Response[F]] =
    Kleisli { rq => OptionT(defaultAuthFailure[F].apply(rq).map(_.withEntity(a).some)) }

  private def apiKeyExtractor[F[_]](rq: Request[F]): Option[String] = rq.headers.get(keyHeader).map(_.head.value)

  private def lift[F[_]: Applicative]: Id ~> OptionT[F, *] = new (Id ~> OptionT[F, *]) {
    override def apply[A](fa: Id[A]): OptionT[F, A] = OptionT(fa.some.pure[F])
  }

  def apply[F[_]: Monad](keys: Set[String]): HttpMiddleware[F] =
    originalHandler =>
      Kleisli((apiKeyExtractor[F] _).map(_.pure[Id])).mapK(lift[F]).flatMap {
        case None                             => forbidden(keyMissing)
        case Some(key) if !keys.contains(key) => forbidden(keyDoesntMatch)
        case _                                => originalHandler
      }

  def apply[F[_]: Monad](key: String, keys: String*): HttpMiddleware[F] = apply(Set(key +: keys: _*))
}
