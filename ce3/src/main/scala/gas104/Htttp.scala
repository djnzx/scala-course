package gas104

import cats.effect._
import cats.implicits._
import gas104.Credentials.{account104, meter104}
import gas104.domain.URow
import gas104.domain.api.Data
import gas104.domain.api.Row
import org.http4s.Headers
import org.http4s.MediaType
import org.http4s.Method
import org.http4s.Request
import org.http4s.RequestCookie
import org.http4s.Response
import org.http4s.UrlForm
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.headers._
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.ci.CIStringSyntax

object Htttp {

  /** convenient header builder */
  object PhpSessionId {
    def apply(value: String): RequestCookie = RequestCookie("PHPSESSID", value)
  }

  /** Response[F] => F[String] */
  def body[F[_]: Concurrent](rs: Response[F]): F[String] =
    rs.body
      .through(fs2.text.utf8.decode[F])
      .compile
      .foldMonoid

  def extractCookie(hs: Headers) =
    hs.headers
      .find(h => h.name.contains(ci"cookie"))
      .map(_.value)
      .map(_.split(";").apply(0))
      .map(_.split("="))
      .flatMap {
        case Array(k, v) => (k -> v).some
        case _           => None
      }

  /** Response[F] => F[Option[String]] */
  def extractToken[F[_]: Concurrent](rs: Response[F]) =
    rs.body
      .through(fs2.text.utf8.decode[F])
      .through(fs2.text.lines[F])
      .dropWhile(s => !s.contains("csrf_token"))
      .drop(1)
      .take(1)
      .map(_.split("="))
      .filter(_.length == 2)
      .map(_(1).stripPrefix("\"").stripSuffix("\""))
      .compile
      .last
      .map(maybeToken => (maybeToken, extractCookie(rs.headers)).mapN(_ -> _))

  /** => Option[String] */
  def obtainCsrfToken[F[_]: Async] = {

    import org.http4s.Method

    val uri = uri"https://account.104.ua/ua/login"
    val rq  = Request[F](
      Method.GET,
      uri
    )
    mkHttpClient[F]
      .flatMap(_.run(rq))
      .use(extractToken[F])
  }

  /** => Client[F] */
  def mkHttpClient[F[_]: Async]: Resource[F, Client[F]] =
    BlazeClientBuilder[F].resource

  /** login: */
  def mkRequestLogin[F[_]](login: String, password: String, csrfToken: String, cookie: (String, String)): Request[F] = {
    val url = uri"https://account.104.ua/ua/login"
    val headers = Headers(
      `Content-Type`(MediaType.application.`x-www-form-urlencoded`),
      Cookie(RequestCookie(cookie._1, cookie._2)),
    )
    val form = UrlForm(
      "username" -> login,
      "password" -> password,
      "_csrf_token" -> csrfToken,
    )
    Request[F](Method.POST, url)
      .withHeaders(headers)
      .withEntity(form)
  }

  /** meters: sessionId => Request[F] */
  def mkRequestData[F[_]](phpSessionId: String): Request[F] = {

    val url = uri"https://ok.104.ua/ua/ajx/individual/meterage/history/remote"

    def mkHeaders(value: String) = Headers(
      `Accept`(MediaType.application.json),
      `Content-Type`(MediaType.application.`x-www-form-urlencoded`),
      `Cookie`(PhpSessionId(value)),
    )

    val payloadMeters = UrlForm(
      "account_no"  -> account104,
      "meter_no"    -> meter104,
      "period_type" -> "y",
//    "end_date"    -> "2023-12-31T23:59:59.999Z"
    )

    Request[F](Method.POST, url)
      .withHeaders(mkHeaders(phpSessionId))
      .withEntity(payloadMeters)
  }

  def obtainData[F[_]: Concurrent](sessionId: String)(client: Client[F]): F[Data] = {
    import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
    client.expect[Data](mkRequestData[F](sessionId))
  }

  def representData[F[_]: Sync](payload: Data): F[Unit] =
    payload.data match {
      case Some(data) =>
        data.traverse_ { x: Row =>
          val r    = URow.from(x)
          val line = (r.dateTime.toLocalDate, r.counter, r.delta)
          Sync[F].delay(pprint.pprintln(line))
        }
      case None       =>
        Sync[F].delay(pprint.pprintln("Error" -> payload))
    }

}
