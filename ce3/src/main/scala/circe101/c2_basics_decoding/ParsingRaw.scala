package circe101.c2_basics_decoding

import cats.implicits.catsSyntaxOptionId
import circe101.Base
import io.circe.CursorOp.DownField
import io.circe.{CursorOp, DecodingFailure, Json, ParsingFailure}

class ParsingRaw extends Base {

  test("primitives") {
    val raw = "33"

    val parsed: Either[ParsingFailure, Json] = io.circe.parser.parse(raw)

    pprint.log(parsed)

    inside(parsed) {
      case Right(x) =>
        x.isNumber shouldBe true
        x.asNumber.flatMap(_.toLong) shouldBe 33.some
    }

  }

  test("strings") {
    val raw =
      """
        |"scala"
        |""".stripMargin

    val parsed: Either[ParsingFailure, Json] = io.circe.parser.parse(raw)

    pprint.log(parsed)

    inside(parsed) {
      case Right(x) =>
        x.isString shouldBe true
        x.asString shouldBe "scala".some
    }

  }

  test("objects - accessing properties") {
    val raw =
      """{
        |  "x" : 1,
        |  "y" : 2.5,
        |  "z" : true,
        |  "name" : "Jim"
        |}
        |""".stripMargin

    val parsed: Either[ParsingFailure, Json] = io.circe.parser.parse(raw)

    pprint.log(parsed)

    inside(parsed) {
      case Right(j) =>
        j.isObject shouldBe true

        inside(j.hcursor.get[Int]("x")) {
          case Right(x) => x shouldBe 1
        }

        inside(j.hcursor.get[Double]("y")) {
          case Right(y) => y shouldBe 2.5
        }

        inside(j.hcursor.get[Boolean]("z")) {
          case Right(z) => z shouldBe true
        }

        inside(j.hcursor.get[String]("name")) {
          case Right(z) => z shouldBe "Jim"
        }

        inside(j.hcursor.get[String]("blah")) {
          case Left(DecodingFailure(e)) =>
            val (reason, errors) = e
            reason shouldBe "Missing required field"
            errors shouldBe List(DownField("blah"))
        }
    }

  }

}
