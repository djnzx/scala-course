package app

import cats.effect.IO
import org.http4s.jetty.server.JettyBuilder

object JettyServer extends MyIOApp {

  override def run: IO[Unit] =
    JettyBuilder[IO]
      .bindHttp(8080)
      .mountService(TestRoutes[IO], "/")
      .resource
      .use(_ => IO.never)

}
