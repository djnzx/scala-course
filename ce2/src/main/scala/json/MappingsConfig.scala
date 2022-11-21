package json

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class MappingsConfig(mappings: List[Mapping])
object MappingsConfig {
  implicit val e: Encoder[MappingsConfig] = deriveEncoder
  implicit val d: Decoder[MappingsConfig] = deriveDecoder
}
