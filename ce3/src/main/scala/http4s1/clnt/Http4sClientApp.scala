package http4s1.clnt

import cats.effect.IO
import cats.effect.IOApp
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.Header
import org.http4s.Headers
import org.http4s.Method
import org.http4s.Request
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sLiteralsSyntax

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Http4sClientApp extends IOApp.Simple {

  val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))

  case class IpLookupResponse(countryCode: String)

  object IpLookupResponse {
    implicit val cfg: Configuration = Configuration.default.withSnakeCaseMemberNames
    implicit val e: Encoder[IpLookupResponse] = deriveConfiguredEncoder
    implicit val d: Decoder[IpLookupResponse] = deriveConfiguredDecoder
  }

  val request = Request[IO](
    Method.GET,
    uri"https://api.vidiq.com/subscriptions/ip-lookup",
    headers = Headers.of(Header("X-Vidiq-Api-Key", "vidiq-ruby:oFj8KV85EF%hL*")),
  )

  override def run: IO[Unit] = BlazeClientBuilder[IO](ec)
    .resource
    .use { client =>
      for {
        r <- client.expect[IpLookupResponse](request)
        _ <- IO { println(r.countryCode) }
      } yield ()
    }

}
