package circe101.c3_manual_creation

import circe101.Base
import io.circe.syntax.EncoderOps
import io.circe.{Json, JsonObject}

class CreatingManually extends Base {

  test("jsonObject") {

    val jo: JsonObject = JsonObject(
      "a" -> 1.asJson,
      "b" -> "Hello".asJson,
      "c" -> true.asJson
    )

    val j: Json = jo.asJson

    val str =
      """{
       |  "a" : 1,
       |  "b" : "Hello",
       |  "c" : true
       |}
       |""".stripMargin.trim

    j.spaces2 shouldBe str
  }

  test("json") {

    val j: Json = Json.obj(
      "a" -> 1.asJson,
      "b" -> "Hello".asJson,
      "c" -> true.asJson
    )

    val str =
      """{
       |  "a" : 1,
       |  "b" : "Hello",
       |  "c" : true
       |}
       |""".stripMargin.trim

    j.spaces2 shouldBe str
  }

}
