package _circe

import io.circe.generic.extras.semiauto.{deriveEnumerationDecoder, deriveEnumerationEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}

object CirceADT extends App {

  sealed trait OrderStatus
  case object Placed extends OrderStatus
  case object Shipped extends OrderStatus
  object OrderStatus {
    implicit val encoder: Encoder[OrderStatus] = deriveEnumerationEncoder
    implicit val decoder: Decoder[OrderStatus] = deriveEnumerationDecoder
  }

  val status: OrderStatus = Placed

  val s1 = status.asJson.noSpaces
  println(s1)                // "Placed"
  val s2 = status.toString
  println(s2)                // Placed
  val s3 = s1.stripPrefix("\"").stripSuffix("\"")
  println(s3)                // Placed

}
