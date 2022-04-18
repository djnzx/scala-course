package app

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router

object Http4sApp extends IOApp {

  /** http://localhost:8080/hello?f=Apple */
  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "hello" :? FruitParamMatcher(f) =>
      Ok(s"fruit given: $f")
    case _                                             => NotFound()
  }

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withPort(8080)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
