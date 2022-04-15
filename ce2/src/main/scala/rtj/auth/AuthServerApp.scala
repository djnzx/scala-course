package rtj.auth

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._

object AuthServerApp extends IOApp {

  val as: AuthService = AuthService()
  val ar: AuthRoutes[IO] = new AuthRoutes[IO](as)
  val app: HttpApp[IO] = ar.routes.orNotFound

  val server = EmberServerBuilder
    .default[IO]
    .withPort(8080)
    .withHttpApp(app)
    .build
    .use(_ => IO.never)

  override def run(args: List[String]): IO[ExitCode] = server

}
