package circe101.c1_basics_encoding

import cats.implicits.catsSyntaxOptionId
import circe101.Base
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps

class ConfigurableEncodingNull extends Base {

  test("automatic encoding, optional fields, by default, being represented as null") {
    case class Person(name: String, knowledge: Option[String] = None)
    object Person {
      implicit val encoder: Encoder[Person] = deriveEncoder
    }

    val p1 = Person("Jim", "Scala".some)
    p1.asJson.spaces2 shouldBe
      """{
        |  "name" : "Jim",
        |  "knowledge" : "Scala"
        |}
        |""".stripMargin.trim

    val p2 = Person("Jim")
    p2.asJson.spaces2 shouldBe
      """{
        |  "name" : "Jim",
        |  "knowledge" : null
        |}
        |""".stripMargin.trim

  }

  // deriveConfiguredEncoder[Person].mapJson(_.dropNullValues)
  test("automatic encoding, optional fields, can be configured to omit") {
    case class Person(name: String, knowledge: Option[String] = None)
    object Person {
      implicit val encoder: Encoder[Person] = deriveEncoder[Person].mapJson(_.dropNullValues)
    }

    val p1 = Person("Jim")
    p1.asJson.spaces2 shouldBe
      """{
        |  "name" : "Jim"
        |}
        |""".stripMargin.trim

  }

}
