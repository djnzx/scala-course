package circex

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax.EncoderOps

/** need to import "circe-generic-extras" */
object C04EncodeDecodeConfigured extends App {

  /** model */
  case class Details(userId: Int)
  val details = Details(33)

  /** configuration relates to encoding and decoding as well */

  {
    implicit val c: Configuration = Configuration.default.withDefaults
    implicit val e: Encoder[Details] = deriveConfiguredEncoder
    implicit val d: Decoder[Details] = deriveConfiguredDecoder

    println(details.asJson) // userId
  }

  {
    implicit val c: Configuration = Configuration.default.withKebabCaseMemberNames
    implicit val e: Encoder[Details] = deriveConfiguredEncoder
    implicit val d: Decoder[Details] = deriveConfiguredDecoder

    println(details.asJson) // user-id
  }

  {
    implicit val c: Configuration = Configuration.default.withSnakeCaseMemberNames
    implicit val e: Encoder[Details] = deriveConfiguredEncoder
    implicit val d: Decoder[Details] = deriveConfiguredDecoder

    println(details.asJson) // user_id
  }

}
