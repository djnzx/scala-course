package http4middle

import cats.data.Kleisli
import cats.data.OptionT
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpRoutes
import org.http4s.Request
import org.http4s.Response
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext.{global => ec}

object Http4sApp extends IOApp.Simple {

  /** wire to the routes */
  val service = new HttpServiceBinding[IO]
  val coreRoutes: HttpRoutes[IO] = service.httpBinding
  val coreRoutes0: HttpRoutes[IO] = HttpRoutes.of(service.httpBinding0)

  val allRoutes = Router(
    "/"  -> coreRoutes,
    "/0" -> coreRoutes0,
  )

  val app: IO[Unit] = for {
    implicit0(logger: Logger[IO]) <- Slf4jLogger.create[IO]

    middleware = LoggerMiddleware[IO]
    loggedRoutes: Kleisli[OptionT[IO, *], Request[IO], Response[IO]] = middleware(allRoutes)
    wholeAppRoutes: Kleisli[IO, Request[IO], Response[IO]] = loggedRoutes.orNotFound
    _ <- logger.info("starting...")
    _ <- BlazeServerBuilder[IO]
      .withExecutionContext(ec)
      .bindHttp(8080, "localhost")
      .withHttpApp(wholeAppRoutes)
      .serve
      .compile
      .drain
  } yield ()

  /** entry point */
  override def run: IO[Unit] = app
}
