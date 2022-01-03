package rtj.auth

import cats.effect.Sync
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder

import java.util.UUID

object AuthApi {

  case class AuthRequest(user: String, pass: String)
  object AuthRequest extends AutoDerivation {
    implicit def enityEncoder[F[_]]: EntityEncoder[F, AuthRequest] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, AuthRequest] = jsonOf
  }

  sealed trait AuthResponse
  case class AuthSuccess(token: UUID) extends AuthResponse
  object AuthSuccess extends AutoDerivation {
    implicit def enityEncoder[F[_]]: EntityEncoder[F, AuthSuccess] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, AuthSuccess] = jsonOf
  }
  case class AuthFailed(message: String) extends AuthResponse
  object AuthFailed extends AutoDerivation {
    implicit def enityEncoder[F[_]]: EntityEncoder[F, AuthFailed] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, AuthFailed] = jsonOf
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
      Decoder[AuthFailed].widen,
    ).reduceLeft(_ or _)

    implicit def enityEncoder[F[_]]: EntityEncoder[F, AuthResponse] = jsonEncoderOf
    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, AuthResponse] = jsonOf
  }

}
