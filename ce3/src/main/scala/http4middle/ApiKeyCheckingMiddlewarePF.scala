package http4middle

import cats.Monad
import cats.implicits._
import http4middle.HttpRoutesWithMiddleware._
import io.circe.Encoder
import org.http4s.Request
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.implicits._
import org.http4s.server.AuthMiddleware.defaultAuthFailure

object ApiKeyCheckingMiddlewarePF {

  /** header to look for in the request */
  private val keyHeader = "ApiKey".ci

  /** message when header is absent */
  private val keyMissing = s"Request should contain API key in the header $keyHeader"

  /** message when header contains a wrong value */
  private val keyDoesntMatch = "API doesn't match"

  def apply[F[_]: Monad](keys: Set[String]): MiddleWare[F] = {

    def forbidden[A: Encoder](a: A): Handler[F] =
      (rq: Request[F]) => defaultAuthFailure[F].apply(rq).map(_.withEntity(a))

    def apiKeyExtractor(rq: Request[F]): Option[String] =
      rq.headers.get(keyHeader).map(_.head.value)

    (originalHandler: Handler[F]) => { (request: Request[F]) =>
      apiKeyExtractor(request) match {
        case None                             => forbidden(keyMissing).apply(request)
        case Some(key) if !keys.contains(key) => forbidden(keyDoesntMatch).apply(request)
        case _                                => originalHandler(request)
      }
    }
  }

  def apply[F[_]: Monad](key: String, keys: String*): MiddleWare[F] = apply(Set(key +: keys: _*))
}
