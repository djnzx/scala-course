package httpfs3

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.semigroupk._
import org.http4s.{HttpApp, Response}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object ServerApp extends IOApp {

  val service: ServiceA[IO] = ServiceA.impl[IO]

  val routes: HttpApp[IO] = Router(
    "/a" -> ServerRoutes.routeA[IO](service),
    "/b" -> ServerRoutes.routeB[IO],
    "/c" -> ServerRoutes.routeC[IO],
    "/d" -> ServerRoutes.routeD[IO],
  ).orNotFound

  val context: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](context)
      .bindHttp(8090, "localhost")
      .withHttpApp(routes)
//      .serve
//      .compile
//      .drain
//      .as(ExitCode.Success)
      .resource
      .use(_ => IO.never)
}
