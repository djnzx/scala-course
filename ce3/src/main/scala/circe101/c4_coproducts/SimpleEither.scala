package circe101.c4_coproducts

import cats.implicits.catsSyntaxEitherId
import circe101.Base
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.syntax.EncoderOps

class SimpleEither extends Base {

  object domain {
    val right: Either[Boolean, Int] = 3.asRight[Boolean]
    val left: Either[Boolean, Int] = true.asLeft[Int]

    val rightJson =
      """{
        |  "Right" : {
        |    "value" : 3
        |  }
        |}
        |""".stripMargin.trim

    val leftJson =
      """{
        |  "Left" : {
        |    "value" : true
        |  }
        |}
        |""".stripMargin.trim
  }

  test("either - encoding") {
    import domain._

    right.asJson.spaces2 shouldBe rightJson
    left.asJson.spaces2 shouldBe leftJson
  }

  test("either - decoding") {
    import domain._

    io.circe.parser.decode[Either[Boolean, Int]](leftJson) shouldBe left
    io.circe.parser.decode[Either[Boolean, Int]](rightJson) shouldBe right
  }

}
