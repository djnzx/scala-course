package circex

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax.EncoderOps

object C07ManualDecoder extends App {

  case class Thing(name: String, value: Int)

  implicit val encodeFoo: Encoder[Thing] = new Encoder[Thing] {
    final def apply(a: Thing): Json = Json.obj(
      ("name", Json.fromString(a.name)),
      ("value", Json.fromInt(a.value)),
    )
  }

  val data = Thing("Hello", 44)

  println(data.asJson.noSpaces) // {"name":"Hello","value":44}
}
