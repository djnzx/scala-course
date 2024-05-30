package http4s101

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io.->
import org.http4s.dsl.io._
import org.http4s.implicits._

object Http4sLauncher extends IOApp.Simple {

  val routes = HttpRoutes.of[IO] {
    // unparsed url
    case GET -> path =>
      path.segments
      pprint.log(path)
      Ok(path.segments.mkString)

//    // only first segment (String)
//    case GET -> Root / path =>
//      pprint.log(path)
//      Ok(path)
  }

  /** http server is no more than a Stream of Requests + f: Request => Response */
  override def run: IO[Unit] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .serve
      .compile
      .drain

}
