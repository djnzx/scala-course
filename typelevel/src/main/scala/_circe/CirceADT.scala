package _circe

import _circe.NoQuotes.StringStripQuotesSyntax
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.extras.semiauto.deriveEnumerationDecoder
import io.circe.generic.extras.semiauto.deriveEnumerationEncoder
import io.circe.syntax.EncoderOps

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
  println(s1) // "Placed"
//  val s2 = status.toString
//  println(s2) // Placed
  val s3a = status.asJson.noSpaces.noQuotes
  println(s3a) // Placed
}
