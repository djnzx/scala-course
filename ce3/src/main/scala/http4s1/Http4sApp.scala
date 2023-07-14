package http4s1

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.{global => ec}

object Http4sApp extends IOApp {

  /** wire to the routes */
  val service = new HttpServiceBinding[IO]
  val coreRoutes: HttpRoutes[IO] = service.httpBinding
  val coreRoutes0: HttpRoutes[IO] = HttpRoutes.of(service.httpBinding0)

  /** wire to the whole routes
    * {{{
    *   HttpApp[F[_]] = Http[F, F]
    *   Http[F[_], G[_]] = Kleisli[F, Request[G], Response[G]]
    * }}}
    */
  val allRoutes: HttpApp[IO] = Router(
    "/"  -> coreRoutes,
    "/0" -> coreRoutes0,
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
