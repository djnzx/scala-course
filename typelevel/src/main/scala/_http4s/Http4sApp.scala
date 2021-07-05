package _http4s

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Http4sApp extends IOApp {

  val coreRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
    case _ => NotFound()
  }

  val allRoutes = Router(
    "/" -> coreRoutes
  ).orNotFound

  val ec = scala.concurrent.ExecutionContext.global

  val httpStream: fs2.Stream[IO, ExitCode] = BlazeServerBuilder[IO](ec)
    .bindHttp(8080, "localhost")
    .withHttpApp(allRoutes)
    .serve

  val app = httpStream
    .compile
    .drain
    .map(_ => ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] = app

}
