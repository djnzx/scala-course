package circe101.c3_manual_creation

import circe101.Base
import io.circe.Encoder
import io.circe.Json
import io.circe.JsonObject
import io.circe.syntax.EncoderOps

class EncodingManually extends Base {

  test("encoder") {
    case class Thing(name: String, value: Int)

    /** encoder is a simple function */
    implicit val encodeFoo: Encoder[Thing] = (a: Thing) => Json.obj(
      "name" -> Json.fromString(a.name),
      "value" -> Json.fromInt(a.value),
    )

    val data = Thing("Hello", 44)

    data.asJson.spaces2 shouldBe
      """{
         |  "name" : "Hello",
         |  "value" : 44
         |}""".stripMargin.trim
  }
}
