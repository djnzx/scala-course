package httpfs3

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.Header
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.Response
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object ServerApp extends IOApp {

  val service: ServiceA[IO] = ServiceA.impl[IO]

  val d: HttpRoutes[IO] = ServerRoutes.routeD[IO]

  val routes: HttpApp[IO] = Router(
    "/a" -> ServerRoutes.routeA[IO](service),
    "/b" -> ServerRoutes.routeB[IO],
    "/c" -> ServerRoutes.routeC[IO],
    "/d" -> d,
  ).orNotFound

  val context: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](context)
      .bindHttp(8090, "localhost")
      .withHttpApp(routes)
      .resource
      .use(_ => IO.never)
}
