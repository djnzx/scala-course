package schedule.g

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe.Json
import io.circe.generic.AutoDerivation
import org.http4s.{Method, Request}
import org.http4s.Status.Successful
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sLiteralsSyntax

import scala.sys.process.Process

object GoogleToken {

  def get: String = Process("gcloud", List("auth", "print-access-token"))
    .lazyLines
    .head

  lazy val mkHttpClient =
    BlazeClientBuilder[IO].resource

  def mkUrl(token: String) = uri"https://www.googleapis.com/oauth2/v1/tokeninfo"
    .withQueryParam("access_token", token)

  case class Token(scope: String)
  object Token extends AutoDerivation

  def getTokenDetails(token: String) =
    mkHttpClient
      .flatMap(_.run {
        Request[IO](Method.GET, mkUrl(token))
      })
      .use {
        case Successful(rs) => rs.as[Token].map(_.scope)
        case x              => IO(pprint.log(x)) >> IO.raiseError(new RuntimeException("wrong response"))
      }

  def invalidateToken(token: String) =
    mkHttpClient
      .flatMap(_.run {
        Request[IO](
          Method.GET,
          uri"https://accounts.google.com/o/oauth2/revoke"
            .withQueryParam("token", token)
        )
      })
      .use {
        case Successful(rs) => rs.as[Json]
        case x              => IO(pprint.log(x)) >> IO.raiseError(new RuntimeException("wrong response"))
      }
      .unsafeRunSync()

  def getTokenScope(token: String) =
    getTokenDetails(token).unsafeRunSync()

  def printTokenScopes(token: String) = {
    val scope = getTokenScope(token)
    pprint.log(scope)
  }

}
