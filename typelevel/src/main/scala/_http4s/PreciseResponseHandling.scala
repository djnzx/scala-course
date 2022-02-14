package _http4s

import cats.effect.Sync
import cats.implicits._
import io.circe.generic.AutoDerivation
import org.http4s.client.Client
import org.http4s.Request
import org.http4s.Status

class PreciseResponseHandling[F[_]: Sync](httpClient: Client[F]) extends EED[F] {

  sealed trait AccessTokenResponse
  case class AccessTokenProvided(token: String) extends AccessTokenResponse
  case class AccessTokenForbidden(code: String, message: String, details: String) extends AccessTokenResponse
  object AccessTokenProvided extends AutoDerivation
  object AccessTokenForbidden extends AutoDerivation
  case class AccessTokenError(message: String)

  def halt(message: String) =
    Sync[F].raiseError[AccessTokenResponse](new RuntimeException(message))

  def obtainAccessToken(rq: Request[F]): F[Either[AccessTokenError, AccessTokenResponse]] =
    httpClient
      .run(rq)
      .use {
        // 200
        case Status.Ok(response) =>
          response
            .attemptAs[AccessTokenProvided]
            .foldF(
              decodeFailure => halt(s"200: parse error: `${decodeFailure.message}`"),
              _.pure[F].widen[AccessTokenResponse],
            )
        // 403
        case Status.Forbidden(response) =>
          response
            .attemptAs[AccessTokenForbidden]
            .foldF(
              decodeFailure => halt(s"403: parse error: `${decodeFailure.message}`"),
              _.pure[F].widen[AccessTokenResponse],
            )
        // else
        case response =>
          response
            .attemptAs[String]
            .foldF(
              fail => halt(s"${response.status}: failed to get body as string: `${fail.message}`"),
              body => halt(s"${response.status}: `$body`"),
            )
      }
      .attempt
      .map(
        _.leftMap(t => AccessTokenError(t.getMessage)),
      )

}
