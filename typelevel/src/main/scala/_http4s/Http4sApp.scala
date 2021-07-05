package _http4s

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpApp, HttpRoutes, Request, Response}

import scala.concurrent.ExecutionContext.{global => ec}

object Http4sApp extends IOApp {

  /** core function */
  def core(name: String) = s"Hello, $name."

  /** wire http */
  val http: PartialFunction[Request[IO], IO[Response[IO]]] = {
    case GET -> Root / "hello" / name => Ok(core(name))
  }

  /** wire to the routes */
  val coreRoutes: HttpRoutes[IO] = HttpRoutes.of[IO](http)

  /** wire to the whole routes */
  val allRoutes: HttpApp[IO] = Router(
    "/" -> coreRoutes
  ).orNotFound

  /** stream */
  val httpStream: fs2.Stream[IO, ExitCode] = BlazeServerBuilder[IO](ec)
    .bindHttp(8080, "localhost")
    .withHttpApp(allRoutes)
    .serve

  /** application */
  val app: IO[Unit] = httpStream
    .compile
    .drain

  /** entry point */
  override def run(args: List[String]): IO[ExitCode] = app.map(_ => ExitCode.Success)

}
