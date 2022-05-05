package http2

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._

object Http4sApp extends IOApp.Simple {

  class HttpServiceBinding[F[_]: Sync] extends Http4sDsl[F] {

    val routes: HttpRoutes[F] = HttpRoutes.of[F] {
      /** http://localhost:8080/hello */
      case rq @ GET -> Root / "hello" => Ok("Hello")
    }

  }

  /** wire to the routes */
  val http = new HttpServiceBinding[IO].routes

  val app: IO[Unit] = for {
    _ <- BlazeServerBuilder[IO]
           .enableHttp2(true)
           .bindHttp(8080, "localhost")
           .withHttpApp(http.orNotFound)
           .serve
           .compile
           .drain
  } yield ()

  override def run: IO[Unit] = app
}
