package _http4s

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Http4sApp extends IOApp {

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
    case _ => NotFound()
  }

  implicit val ec = scala.concurrent.ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](ec)
      .bindHttp(8080, "localhost")
      .withHttpApp(Router(
        "/" -> routes
      ).orNotFound)
      .serve
      .compile
      .drain
      .map(_ => ExitCode.Success)
}
