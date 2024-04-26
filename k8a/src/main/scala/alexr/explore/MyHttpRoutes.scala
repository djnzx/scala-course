package alexr.explore

import alexr.explore.domain.Quote
import alexr.explore.domain.Quotes
import cats.effect.Sync
import cats.implicits._
import java.net.InetAddress
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class MyHttpRoutes[F[_]: Sync](quotes: Quotes[F]) extends Http4sDsl[F] {

  private val ipF = Sync[F].delay(InetAddress.getLocalHost.getHostAddress)

  val routes = HttpRoutes.of[F] {
    //
    case GET -> Root =>
      val q = for {
        ip <- ipF
        q  <- quotes.next
      } yield Quote(ip, q)
      Ok(q)
  }

}
