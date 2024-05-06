package circe101.c1_basics_encoding

import circe101.Base
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax.EncoderOps

/** configurable naming: encoding / decoding */
class ConfigurableNaming extends Base {

  test("automatic, but configurable (with defaults)") {
    case class Person(userName: String)
    object Person {
      implicit val conf = Configuration.default
      implicit val encoder: Encoder[Person] = deriveConfiguredEncoder
    }

    val p = Person("Jim")
    p.asJson.spaces2 shouldBe
      """{
        |  "userName" : "Jim"
        |}
        |""".stripMargin.trim
  }

  // userName => user_name
  test("automatic, but configurable (makes naming snake)") {
    case class Person(userName: String)
    object Person {
      implicit val conf = Configuration.default.withSnakeCaseMemberNames
      implicit val encoder: Encoder[Person] = deriveConfiguredEncoder
    }

    val p = Person("Jim")
    p.asJson.spaces2 shouldBe
      """{
        |  "user_name" : "Jim"
        |}
        |""".stripMargin.trim
  }

  // userName => user-name
  test("automatic, but configurable (makes naming kebab)") {
    case class Person(userName: String)
    object Person {
      implicit val conf = Configuration.default.withKebabCaseMemberNames
      implicit val encoder: Encoder[Person] = deriveConfiguredEncoder
    }

    val p = Person("Jim")
    p.asJson.spaces2 shouldBe
      """{
        |  "user-name" : "Jim"
        |}
        |""".stripMargin.trim
  }

  // userName => UserName
  test("automatic, but configurable (makes naming pascal)") {
    case class Person(userName: String)
    object Person {
      implicit val conf = Configuration.default.withPascalCaseMemberNames
      implicit val encoder: Encoder[Person] = deriveConfiguredEncoder
    }

    val p = Person("Jim")
    p.asJson.spaces2 shouldBe
      """{
        |  "UserName" : "Jim"
        |}
        |""".stripMargin.trim
  }

  // userName => USER_NAME
  test("automatic, but configurable (makes naming screaming snake)") {
    case class Person(userName: String)
    object Person {
      implicit val conf = Configuration.default.withScreamingSnakeCaseMemberNames
      implicit val encoder: Encoder[Person] = deriveConfiguredEncoder
    }

    val p = Person("Jim")
    p.asJson.spaces2 shouldBe
      """{
        |  "USER_NAME" : "Jim"
        |}
        |""".stripMargin.trim
  }

}
