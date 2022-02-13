package _circe

import io.circe.generic.AutoDerivation
import io.circe.generic.extras.semiauto.deriveEnumerationDecoder
import io.circe.generic.extras.semiauto.deriveEnumerationEncoder
import io.circe.syntax.EncoderOps
import io.circe.Decoder
import io.circe.Encoder

object CirceADT extends App {

  sealed trait OrderStatus
  case object Placed extends OrderStatus
  case object Shipped extends OrderStatus
  object OrderStatus {
    implicit val encoder: Encoder[OrderStatus] = deriveEnumerationEncoder
    implicit val decoder: Decoder[OrderStatus] = deriveEnumerationDecoder
  }

  val status: OrderStatus = Placed

  val s1 = status.asJson.noSpaces
  println(s1) // "Placed"
  val s2 = status.toString
  println(s2) // Placed
  val s3 = s1.stripPrefix("\"").stripSuffix("\"")
  println(s3) // Placed

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
