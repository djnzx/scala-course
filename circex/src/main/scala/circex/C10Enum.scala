package circex

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.extras.semiauto.deriveEnumerationDecoder
import io.circe.generic.extras.semiauto.deriveEnumerationEncoder
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps

object C10Enum extends App {

  sealed trait OrderStatus
  case object Placed extends OrderStatus
  case object Shipped extends OrderStatus
  object OrderStatus {
    implicit val encoder: Encoder[OrderStatus] = deriveEnumerationEncoder
    implicit val decoder: Decoder[OrderStatus] = deriveEnumerationDecoder
  }

  val status1: OrderStatus = Placed
  val status2: OrderStatus = Shipped

  println(status1.asJson.noSpaces) // "Placed"
  println(status2.asJson.noSpaces) // "Shipped"

  val raw1 =
    """
      |"Placed"
      |""".stripMargin

  val raw2 =
    """
      |"Shipped"
      |""".stripMargin

  val s1 = decode[OrderStatus](raw1)
  val s2 = decode[OrderStatus](raw2)
  val s3 = decode[OrderStatus]("...")
  pprint.pprintln(s1)
  pprint.pprintln(s2)
  pprint.pprintln(s3)
}
