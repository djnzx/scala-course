package _http4s

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import scala.concurrent.ExecutionContext.{global => ec}

object Http4sApp extends IOApp {

  val coreRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
    case _ => NotFound()
  }

  val allRoutes = Router(
    "/" -> coreRoutes
  ).orNotFound

  val httpStream: fs2.Stream[IO, ExitCode] = BlazeServerBuilder[IO](ec)
    .bindHttp(8080, "localhost")
    .withHttpApp(allRoutes)
    .serve

  val app: IO[Unit] = httpStream
    .compile
    .drain

  override def run(args: List[String]): IO[ExitCode] = app.map(_ => ExitCode.Success)

}
