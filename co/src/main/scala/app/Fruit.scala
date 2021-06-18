package app

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto.{deriveEnumerationDecoder, deriveEnumerationEncoder}

sealed trait Fruit

object Fruit {
  implicit val encoder: Encoder[Fruit] = deriveEnumerationEncoder
  implicit val decoder: Decoder[Fruit] = deriveEnumerationDecoder
}

case object Apple extends Fruit
case object Plum extends Fruit
