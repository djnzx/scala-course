package circe101.c4_coproducts

import cats.implicits.catsSyntaxEitherId
import circe101.Base
import io.circe.Decoder
import io.circe.Decoder.Result
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.HCursor
import io.circe.JsonObject
import io.circe.syntax.EncoderOps

class ConfigurableEither extends Base {

  object domain {
    val right: Either[Boolean, Int] = 3.asRight[Boolean]
    val left: Either[Boolean, Int] = true.asLeft[Int]

    val rightJson =
      """{
        |  "value" : 3
        |}
        |""".stripMargin.trim

    val leftJson =
      """{
        |  "value" : true
        |}
        |""".stripMargin.trim

  }

  test("either - encoding with custom encoder") {
    import domain._

    implicit val encoder: Encoder[Either[Boolean, Int]] = Encoder.instance {

      case Right(value) => JsonObject(
          "value" -> value.asJson
        ).asJson

      case Left(value) => JsonObject(
          "value" -> value.asJson
        ).asJson

    }

//    pprint.log(right.asJson.spaces2)
//    pprint.log(left.asJson.spaces2)
    right.asJson.spaces2 shouldBe rightJson
    left.asJson.spaces2 shouldBe leftJson
  }

  test("either - decoding with custom decoder") {
    import domain._

    object bits {
      def tryBool(h: HCursor) = h.downField("value").as[Boolean]
      def tryInt(h: HCursor) = h.downField("value").as[Int]
    }

    // combining via Either.orElse
    val decoder1: Decoder[Either[Boolean, Int]] = Decoder.instance { h =>
      val eitherBool: Either[DecodingFailure, Either[Boolean, Nothing]] = bits.tryBool(h).map(x => Left(x))
      val eitherInt: Either[DecodingFailure, Either[Nothing, Int]] = bits.tryInt(h).map(x => Right(x))

      val r: Either[DecodingFailure, Either[Boolean, Int]] = eitherBool orElse eitherInt
      r
    }

    // combining via List.reduce
    val decoder2: Decoder[Either[Boolean, Int]] = Decoder.instance { h =>
      val eitherBool: Either[DecodingFailure, Either[Boolean, Nothing]] = bits.tryBool(h).map(x => Left(x))
      val eitherInt: Either[DecodingFailure, Either[Nothing, Int]] = bits.tryInt(h).map(x => Right(x))

      List(
        eitherBool,
        eitherInt
      ).reduce(_ orElse _)
    }

    // combining via List.fold
    implicit val decoder3: Decoder[Either[Boolean, Int]] = Decoder.instance { h: HCursor =>
      val eitherBoolF: HCursor => Either[DecodingFailure, Left[Boolean, Nothing]] = (bits.tryBool _).andThen(_.map(x => Left(x)))
      val eitherIntF: HCursor => Either[DecodingFailure, Right[Nothing, Int]] = (bits.tryInt _).andThen(_.map(x => Right(x)))
      val alwaysFailedF: HCursor => Result[Either[Boolean, Int]] = Decoder.failedWithMessage[Either[Boolean, Int]]("stub...").apply _

      val finalF = List(
        eitherBoolF,
        eitherIntF
      ).foldLeft(alwaysFailedF) { (f1, f2) =>
        (hc: HCursor) => f1(hc) orElse f2(hc)
      }
      finalF.apply(h)
    }

    def parse(raw: String) =
      io.circe.parser.decode[Either[Boolean, Int]](raw)

    inside(parse(leftJson)) {
      case Right(x) => x shouldBe left
    }

    inside(parse(rightJson)) {
      case Right(x) => x shouldBe right
    }

  }

}
