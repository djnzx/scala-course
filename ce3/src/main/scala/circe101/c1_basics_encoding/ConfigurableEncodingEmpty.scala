package circe101.c1_basics_encoding

import circe101.Base
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps

class ConfigurableEncodingEmpty extends Base {

  test("automatic encoding, empty arrays, by default are encoded with []") {
    case class Person(name: String, knowledge: List[String])
    object Person {
      implicit val encoder: Encoder[Person] = deriveEncoder[Person]
    }

    val p1 = Person("Jim", List())
    p1.asJson.spaces2 shouldBe
      """{
        |  "name" : "Jim",
        |  "knowledge" : [
        |  ]
        |}
        |""".stripMargin.trim

  }

  // deriveEncoder[Person].mapJson(_.dropEmptyValues)
  test("automatic encoding, empty arrays, can be configured to omit") {
    case class Person(name: String, knowledge: List[String])
    object Person {
      implicit val encoder: Encoder[Person] = deriveEncoder[Person].mapJson(_.dropEmptyValues)
    }

    val p1 = Person("Jim", List())
    p1.asJson.spaces2 shouldBe
      """{
        |  "name" : "Jim"
        |}
        |""".stripMargin.trim

  }

}
