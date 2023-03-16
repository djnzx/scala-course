package app

import cats.effect.IO
import org.http4s.blaze.server.BlazeServerBuilder

object BlazeServer extends MyIOApp {

  override def run: IO[Unit] = BlazeServerBuilder[IO]
    .bindHttp(8080, "0.0.0.0")
    .withHttpApp(TestRoutes[IO].orNotFound)
    .resource
    .use(_ => IO.never)

}
