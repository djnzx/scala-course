package _circe

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax.EncoderOps

object CirceADT2 extends App {

  sealed trait AccessTokenResponse

  object AccessTokenResponse {
    import cats.implicits._

    implicit val encoder: Encoder[AccessTokenResponse] = Encoder.instance {
      case x: AccessTokenProvided  => x.asJson
      case x: AccessTokenForbidden => x.asJson
    }

    implicit val decoder: Decoder[AccessTokenResponse] = List[Decoder[AccessTokenResponse]](
      Decoder[AccessTokenProvided].widen,
      Decoder[AccessTokenForbidden].widen,
    ).reduceLeft(_ or _)

  }
  case class AccessTokenProvided(access_token: String) extends AccessTokenResponse
  object AccessTokenProvided extends AutoDerivation
  case class AccessTokenForbidden(code: String, message: String, details: String) extends AccessTokenResponse
  object AccessTokenForbidden extends AutoDerivation

}
