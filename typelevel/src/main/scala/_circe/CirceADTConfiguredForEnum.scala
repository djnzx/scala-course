package _circe

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEnumerationCodec
import io.circe.syntax.EncoderOps

object CirceADTConfiguredForEnum extends App {

  implicit class StringCapitalizeDeCapitalizeSyntax(s: String) {
    def capitalize: String = s match {
      case s if s.isEmpty => s
      case s =>
        val chars: Array[Char] = s.toCharArray
        chars(0) = Character.toUpperCase(chars(0))
        new String(chars)
    }
    def deCapitalize: String = s match {
      case s if s.isEmpty => s
      case s =>
        val chars: Array[Char] = s.toCharArray
        chars(0) = Character.toLowerCase(chars(0))
        new String(chars)
    }
  }

  sealed trait OrderStatus
  case object OrderPlaced extends OrderStatus
  case object OrderShipped extends OrderStatus

  object OrderStatus {
    private implicit val config: Configuration =
      Configuration.default.copy(transformConstructorNames = _.deCapitalize)

    implicit val modeCodec: Codec[OrderStatus] = deriveEnumerationCodec
  }

  val status: OrderStatus = OrderPlaced

  val s1 = status.asJson.noSpaces
  println(s1) // "Placed"
}
