package circe101.c5_adt

import circe101.Base
import io.circe
import io.circe.parser
import io.circe.syntax._

class V1DiscriminatorBased extends Base {
  import Model._

  val data: Event = Event.Foo(33)

  test("non-configured ADT - encode") {
    import io.circe.generic.auto._

    data.asJson.spaces2 shouldBe
      """
        |{
        |  "Foo" : {
        |    "i" : 33
        |  }
        |}
        |""".stripMargin.trim
  }

  test("configured ADT - encode/decode") {
    import io.circe.generic.extras.Configuration
    import io.circe.generic.extras.auto._
    implicit val c: Configuration = Configuration.default.withDiscriminator("type")

    val json: String =
      """
        |{
        |  "i" : 33,
        |  "type" : "Foo"
        |}
        |""".stripMargin.trim

    data.asJson.spaces2 shouldBe json

    val decoded: Either[circe.Error, Event] = parser.decode[Event](json)
    decoded shouldBe Right(data)
  }

}
