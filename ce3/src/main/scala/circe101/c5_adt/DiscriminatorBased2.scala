package circe101.c5_adt

import cats.implicits.catsSyntaxEitherId
import circe101.Base
import io.circe.Decoder
import io.circe.Encoder
import io.circe.Json
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax.EncoderOps

// https://circe.github.io/circe/codecs/adt.html
class DiscriminatorBased2 extends Base {

  // BEWARE codecs derivation HAS a problems whn it's used inside scalatest closures
  object domain {

    sealed trait PropVal
    object PropVal {
      case class SportId(value: Int)    extends PropVal
      case class RoundId(value: Int)    extends PropVal
      case class MoneyId(value: Double) extends PropVal

      implicit val config: Configuration = Configuration.default.withDiscriminator("type")
      implicit val end: Encoder[PropVal] = deriveConfiguredEncoder
      implicit val dec: Decoder[PropVal] = deriveConfiguredDecoder
    }

  }

  test("playground") {
    import domain._
    import PropVal._

    val xs: List[PropVal] = List(
      SportId(123),
      RoundId(456),
      MoneyId(7.8)
    )

    val js = List(
      Json.obj("value" -> 123.asJson, "type" -> "SportId".asJson),
      Json.obj("value" -> 456.asJson, "type" -> "RoundId".asJson),
      Json.obj("value" -> 7.8.asJson, "type" -> "MoneyId".asJson),
    )

    (xs zip js).foreach { case (o, jo) =>
      val jo2 = o.asJson
      jo2 shouldBe jo
      jo.as[PropVal] shouldBe o.asRight
    }
  }

}
