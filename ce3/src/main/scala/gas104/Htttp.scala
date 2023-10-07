package gas104

import org.http4s.Headers
import org.http4s.MediaType
import org.http4s.Method
import org.http4s.Request
import org.http4s.RequestCookie
import org.http4s.UrlForm
import org.http4s.headers._
import org.http4s.implicits.http4sLiteralsSyntax

object Htttp {

  object PhpSessionId {
    def apply(value: String): RequestCookie = RequestCookie("PHPSESSID", value)
  }

  val uri     = uri"https://ok.104.ua/ua/ajx/individual/meterage/history/remote"
  val headers = Headers(
    `Accept`(MediaType.application.json),
    `Content-Type`(MediaType.application.`x-www-form-urlencoded`),
    `Cookie`(PhpSessionId("")),
  )

  val payload = UrlForm(
    "account_no"  -> "", // 0800xxxxxx
    "meter_no"    -> "",
    "period_type" -> "y",
    "end_date"    -> "2023-12-31T23:59:59.999Z"
  )

  def rq[F[_]] = Request[F](Method.POST, uri)
    .withHeaders(headers)
    .withEntity(payload)

}
