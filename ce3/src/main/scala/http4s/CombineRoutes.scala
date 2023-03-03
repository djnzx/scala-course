package http4s

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl

class HealthApi[F[_]: Monad] extends Http4sDsl[F] {

  def service: HttpRoutes[F] = HttpRoutes.of { case GET -> Root / "ping" => Ok("pong") }

}

class ServiceApi[F[_]: Monad] extends Http4sDsl[F] {

  def service: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "hello" / name => Ok(s"Hello $name")
    case GET -> Root / "user"         => Ok("admin@exam.com")
  }

}

object Main extends IOApp.Simple {

  val service = (new HealthApi[IO].service <+> new ServiceApi[IO].service) orNotFound

  override def run: IO[Unit] = {
    BlazeServerBuilder[IO]
      .withHttpApp(service)
      .bindHttp(8080)
      .serve
      .compile
      .drain
  }

}
