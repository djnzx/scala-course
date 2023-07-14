package rtj_cats.auth

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import io.circe.generic.AutoDerivation
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

/** ADT deserialization */
object AuthApi extends App {

  case class AuthRequest(user: String, pass: String)
  object AuthRequest extends AutoDerivation {
    implicit def entityEncoder[F[_]]: EntityEncoder[F, AuthRequest] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, AuthRequest] = jsonOf
  }

  sealed trait AuthResponse
  case class AuthSuccess(token: UUID) extends AuthResponse
  object AuthSuccess extends AutoDerivation {
    implicit def entityEncoder[F[_]]: EntityEncoder[F, AuthSuccess] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, AuthSuccess] = jsonOf
  }
  case class AuthFailed(message: String) extends AuthResponse
  object AuthFailed extends AutoDerivation {
    implicit def entityEncoder[F[_]]: EntityEncoder[F, AuthFailed] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, AuthFailed] = jsonOf
  }

  object AuthResponse {
    import cats.implicits._

    // explicit encoder
    implicit val encoder: Encoder[AuthResponse] = Encoder.instance {
      case x: AuthSuccess => x.asJson
      case x: AuthFailed  => x.asJson
    }
    // explicit decoder. actually, manually try to use all decoders
    implicit val decoder: Decoder[AuthResponse] = List[Decoder[AuthResponse]](
      Decoder[AuthSuccess].widen,
      Decoder[AuthFailed].widen
    ).reduceLeft(_ or _)

    implicit def enityEncoder[F[_]]: EntityEncoder[F, AuthResponse] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, AuthResponse] = jsonOf
  }

  val r1: AuthResponse = AuthSuccess(UUID.randomUUID())
  val r2: AuthResponse = AuthFailed("bad credentials")
  val s1 = r1.asJson.noSpaces
  val s2 = r2.asJson.noSpaces
  pprint.pprintln(s1)
  pprint.pprintln(s2)
  val x1 = decode[AuthResponse](s1)
  val x2 = decode[AuthResponse](s2)
  val x3 = decode[AuthResponse]("bad content")
  pprint.pprintln(x1)
  pprint.pprintln(x2)
  pprint.pprintln(x3)

}
