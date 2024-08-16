package app

import cats.effect.IO
import org.http4s.blaze.server.BlazeServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jFactory

object BlazeServer extends MyIOApp {

  implicit val logger = Slf4jFactory.create[IO]

  override def run: IO[Unit] = BlazeServerBuilder[IO]
    .bindHttp(8080, "0.0.0.0")
    .withHttpApp(TestRoutes[IO].orNotFound)
    .resource
    .use(_ => IO.never)

}
