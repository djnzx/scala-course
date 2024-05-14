package app.model

import zio.json.{DeriveJsonDecoder, JsonDecoder}

case class Conditions(
  relation: Relation,
  conditions: Set[Condition[PropVal]]
)

object Conditions extends {

  implicit val decoder: JsonDecoder[Conditions] = DeriveJsonDecoder.gen

}
