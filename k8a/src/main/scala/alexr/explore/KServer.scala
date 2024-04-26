package alexr.explore

import alexr.explore.domain.Quotes
import alexr.explore.domain.Quotes.Impl
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.blaze.server.BlazeServerBuilder

object KServer extends IOApp.Simple {

  val service: Impl[IO] = new Quotes.Impl[IO]
  val http = new MyHttpRoutes[IO](service)

  override def run: IO[Unit] =
    BlazeServerBuilder[IO]
      .bindHttp(port = 8080, host = "0.0.0.0")
      .withHttpApp(http.routes.orNotFound)
      .resource
      .use(_ => IO.never)

}
