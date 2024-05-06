package circe101.c1_basics_encoding

import circe101.Base

class Automatic extends Base {

  test("fully automatic, unconfigurable (imports based)") {

    /** 1. data definition */
    case class Person(name: String, age: Int)

    /** 2. data */
    val p = Person("Jim", 33)

    /** imports to have a syntax `.json` */
    import io.circe.syntax.EncoderOps
    /** imports to do the magic */
    import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec

    /** serialization */
    val json = p.asJson

    /** making string */
    json.spaces2 shouldBe
      """
        |{
        |  "name" : "Jim",
        |  "age" : 33
        |}
        |
        |""".stripMargin.trim
  }

  test("fully automatic, unconfigurable (annotation based)") {

    /** 1. data definition */
    @io.circe.generic.JsonCodec case class Person(name: String, age: Int)

    /** 2. data */
    val p = Person("Jim", 33)

    /** imports to have a syntax `.json` */
    import io.circe.syntax.EncoderOps

    /** serialization */
    val json = p.asJson

    /** making string */
    json.spaces2 shouldBe
      """
        |{
        |  "name" : "Jim",
        |  "age" : 33
        |}
        |
        |""".stripMargin.trim
  }

  test("fully automatic, unconfigurable (explicit, declaration based)") {

    /** 1. data definition */
    case class Person(name: String, age: Int)
    object Person extends io.circe.generic.AutoDerivation

    /** 2. data */
    val p = Person("Jim", 33)

    /** imports to have a syntax `.json` */
    import io.circe.syntax.EncoderOps

    /** serialization */
    val json = p.asJson

    /** making string */
    json.spaces2 shouldBe
      """
        |{
        |  "name" : "Jim",
        |  "age" : 33
        |}
        |
        |""".stripMargin.trim
  }

  test("fully automatic, unconfigurable (more explicit, declaration based)") {

    /** 1. data definition */
    case class Person(name: String, age: Int)
    /** 2. encoders definition */
    object Person {
      implicit val encoder: io.circe.Encoder[Person] = io.circe.generic.semiauto.deriveEncoder
      implicit val decoder: io.circe.Decoder[Person] = io.circe.generic.semiauto.deriveDecoder
    }

    /** 3. data */
    val p = Person("Jim", 33)

    /** 4. imports to have a syntax `.json` */
    import io.circe.syntax.EncoderOps

    /** serialization */
    val json = p.asJson

    /** making string */
    json.spaces2 shouldBe
      """
        |{
        |  "name" : "Jim",
        |  "age" : 33
        |}
        |
        |""".stripMargin.trim
  }

}
