package circex.cases.opt

import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, parser}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object OptionWithoutNullSerializationDeserialization {

  case class Data(author: String, skills: Option[Seq[String]])
  object Data {
    implicit val encoder: Encoder[Data] = deriveEncoder[Data].mapJson(_.dropNullValues)
    implicit val decoder: Decoder[Data] = deriveDecoder
  }

}

class OptionWithoutNullSerializationDeserializationSpec extends AnyFunSpec with Matchers {
  import OptionWithoutNullSerializationDeserialization._

  val data1: Data = Data("Jim", None)

  val data2: Data = Data("Jack", Seq("Java", "Scala").some)

  val json1: String =
    """{
      |  "author" : "Jim"
      |}""".stripMargin

  val json2: String =
    """{
      |  "author" : "Jack",
      |  "skills" : [
      |    "Java",
      |    "Scala"
      |  ]
      |}""".stripMargin

  describe("serialization") {
    it("none case") {
      data1.asJson.spaces2 shouldBe json1
    }
    it("some case") {
      data2.asJson.spaces2 shouldBe json2
    }
  }
  describe("deserialization") {
    it("none case") {
      parser.decode[Data](json1) shouldBe data1.asRight
    }
    it("some case") {
      parser.decode[Data](json2) shouldBe data2.asRight
    }
  }
}