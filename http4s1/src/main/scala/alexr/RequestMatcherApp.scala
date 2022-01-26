package alexr

import cats._
import cats.effect._
import cats.implicits._
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.ci.CIString

class RequestMatcherApp[F[_]: Monad: Concurrent] extends Http4sDsl[F] {

  val Authorization: CIString = CIString("Authorization")
  implicit def anyEncoderToEntityEncoder[A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[A]
  implicit def anyDecoderToEntityDecoder[A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]

  val jsonObject: Json = Json.obj(
    "a" -> 1.asJson,
  )

  val routers: HttpRoutes[F] = HttpRoutes.of {
//    case GET -> Root / "youtube" / "analytics"      => Ok(jsonObject)
//    case POST -> Root / "youtube" / "analytics"     => Ok(jsonObject)
    case rq @ GET -> Root / "youtube" / "analytics" =>
      val ho: Option[Header.Raw] = rq.headers.get(Authorization).map(_.head)
      Ok(
        Json.obj(
          "Authorization" -> ho.map(_.value).getOrElse("no header").asJson,
        ),
      )
  }

}

object RequestMatcherApp extends IOApp.Simple {
  val rqm: RequestMatcherApp[IO] = new RequestMatcherApp[IO]

  val rq: Request[IO] = Request[IO](
    Method.GET,
    uri"/youtube" / "analytics",
    headers = Headers("Authorization" -> "abc"),
  )

  override def run: IO[Unit] = for {
    rs <- rqm.routers(rq).value
    body <- rs.get.body.compile.toList.map(_.toArray).map(new String(_))
    _ <- IO(pprint.pprintln(body))
  } yield ()
}
