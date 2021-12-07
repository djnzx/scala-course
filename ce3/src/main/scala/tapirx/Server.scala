package tapirx

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import tapirx.TapirTeaser.Wiring.countCharactersRoutes

object Server extends IOApp {

  /** http://localhost:8080/hello/Jim */
  val routesPartial: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello: $name")
    case _ => NotFound()
  }

  val routesFull: HttpApp[IO] = Router(
    "/" -> routesPartial,
    "c" -> countCharactersRoutes,
  ).orNotFound

  implicit val ec = scala.concurrent.ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .withExecutionContext(ec) // 0.23.6
      .bindHttp(8080, "localhost")
      .withHttpApp(routesFull)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
