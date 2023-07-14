package app

import cats.effect.IO
import com.comcast.ip4s.IpLiteralSyntax
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.slf4j.Slf4jFactory

object EmberServer extends MyIOApp {

  implicit val lf: Slf4jFactory[IO] = Slf4jFactory.create[IO]

  override def run: IO[Unit] = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(TestRoutes[IO].orNotFound)
    .build
    .use(_ => IO.never)

}
