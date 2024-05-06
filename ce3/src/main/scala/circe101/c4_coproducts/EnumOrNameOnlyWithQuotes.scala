package circe101.c4_coproducts

import circe101.Base
import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto.{deriveEnumerationDecoder, deriveEnumerationEncoder}
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps

class EnumOrNameOnlyWithQuotes extends Base {

  sealed trait OrderStatus
  object OrderStatus {
    case object Placed  extends OrderStatus
    case object Shipped extends OrderStatus

    implicit val encoder: Encoder[OrderStatus] = deriveEnumerationEncoder
    implicit val decoder: Decoder[OrderStatus] = deriveEnumerationDecoder
  }

  val statusPlaced: OrderStatus = OrderStatus.Placed
  val statusShipped: OrderStatus = OrderStatus.Shipped

  val rawPlaced =
    """
      |"Placed"
      |""".stripMargin.trim

  val rawShipped =
    """
      |"Shipped"
      |""".stripMargin.trim

  test("encode") {
    statusPlaced.asJson.spaces2 shouldBe rawPlaced
    statusShipped.asJson.spaces2 shouldBe rawShipped
  }

  test("decode") {
    inside(decode[OrderStatus](rawPlaced)) {
      case Right(x) => x shouldBe OrderStatus.Placed
    }

    inside(decode[OrderStatus](rawShipped)) {
      case Right(x) => x shouldBe OrderStatus.Shipped
    }

    inside(decode[OrderStatus]("blah")) {
      case Left(x) => x.getMessage shouldBe "expected json value got 'blah' (line 1, column 1)"
    }
  }

}
