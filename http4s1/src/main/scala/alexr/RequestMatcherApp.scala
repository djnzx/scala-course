package alexr

import cats._
import cats.effect._
import cats.implicits._
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._

class RequestMatcherApp[F[_]: Monad: Concurrent] extends Http4sDsl[F] {

  implicit def anyEncoderToEntityEncoder[A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[A]
  implicit def anyDecoderToEntityDecoder[A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]

  val jsonObject: Json = Json.obj(
    "a" -> 1.asJson,
  )

  val routers: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "youtube" / "analytics"  => Ok(jsonObject)
    case POST -> Root / "youtube" / "analytics" => Ok(jsonObject)
  }

}
