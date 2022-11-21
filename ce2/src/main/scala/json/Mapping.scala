package json

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Mapping(from: String, to: String)
object Mapping {
  implicit val e: Encoder[Mapping] = deriveEncoder
  implicit val d: Decoder[Mapping] = deriveDecoder
}
