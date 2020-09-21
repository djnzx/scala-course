package mortals

import java.net.URLEncoder

import jsonformat.JsDecoder
import mortals.Mortals004a.UrlEncoded
import scalaz.IList
import simulacrum.typeclass
import eu.timepit.refined
import mortals.Mortals003a.Epoch
import refined.api.Refined
import refined.string.Url

object Mortals004b extends App {
  
  final case class AuthRequest(
    redirect_uri: String Refined Url,
    scope: String,
    client_id: String,
    prompt: String = "consent",
    response_type: String = "code",
    access_type: String = "offline"
  )
  final case class AccessRequest(
    code: String,
    redirect_uri: String Refined Url,
    client_id: String,
    client_secret: String,
    scope: String = "",
    grant_type: String = "authorization_code"
  )
  final case class AccessResponse(
    access_token: String,
    token_type: String,
    expires_in: Long,
    refresh_token: String
  )
  final case class RefreshRequest(
    client_secret: String,
    refresh_token: String,
    client_id: String,
    grant_type: String = "refresh_token"
  )
  final case class RefreshResponse(
    access_token: String,
    token_type: String,
    expires_in: Long
  )
  // https://github.com/anthonynsimon/jurl
  // https://github.com/scalaz/scalaz-deriving/tree/master/examples/jsonformat/src
  // https://scalacenter.github.io/scalafix/docs/rules/DisableSyntax
  // https://github.com/scalaz/scalaz-deriving

  final case class UrlQuery(params: List[(String, String)])

  @typeclass
  trait UrlQueryWriter[A] {
    def toUrlQuery(a: A): UrlQuery
  }
  
  @typeclass
  trait UrlEncodedWriter[A] {
    def toUrlEncoded(a: A): String Refined UrlEncoded
  }
  
  object UrlEncodedWriter {
    implicit val encoded: UrlEncodedWriter[String Refined UrlEncoded] = identity
    implicit val string: UrlEncodedWriter[String] = 
      (s => Refined.unsafeApply(URLEncoder.encode(s, "UTF-8")))
    implicit val url: UrlEncodedWriter[String Refined Url] = ??? 
//      (s => s.value.toUrlEncoded)
    implicit val long: UrlEncodedWriter[Long] = 
      (s => Refined.unsafeApply(s.toString))
    implicit def ilist[K: UrlEncodedWriter, V: UrlEncodedWriter]: UrlEncodedWriter[IList[(K, V)]] =
      ???
//    { m =>
//      val raw = m.map {
//        case (k, v) => k.toUrlEncoded.value + "=" + v.toUrlEncoded.value }.intercalate("&")
//      Refined.unsafeApply(raw) // by deduction
//    }
  }

  trait JsonClient[F[_]] {
    
    def get[A: JsDecoder](
    uri: String Refined Url,
    headers: IList[(String, String)]
  ): F[A]
    
    def post[P: UrlEncodedWriter, A: JsDecoder](
      uri: String Refined Url,
      payload: P,
      headers: IList[(String, String)] = IList.empty
    ): F[A]
    
  }
  final case class CodeToken(token: String, redirect_uri: String Refined Url)
  
  trait UserInteraction[F[_]] {
    def start: F[String Refined Url]
    def open(uri: String Refined Url): F[Unit]
    def stop: F[CodeToken]
  }

  trait LocalClock[F[_]] { 
    def now: F[Epoch]
  }

  final case class ServerConfig(
    auth: String Refined Url,
    access: String Refined Url,
    refresh: String Refined Url,
    scope: String,
    clientId: String,
    clientSecret: String
  )
  final case class RefreshToken(token: String)
  final case class BearerToken(token: String, expires: Epoch)

  println(2)
}
