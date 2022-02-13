package shapelss.coproduct

import cats.implicits.toFunctorOps
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import shapeless.:+:
import shapeless.CNil
import shapeless.ops.coproduct.Inject

import java.util.UUID

object CoproductApp extends App {

  case class Ok(token: UUID)
  object Ok extends AutoDerivation {
    def next = Ok(UUID.randomUUID())
  }
  case class Error(message: String)
  object Error extends AutoDerivation
  case object QuotaExceeded extends AutoDerivation
  case object WrongData {
    implicit val e: Encoder[WrongData.type] = _ => "WrongData".asJson
    implicit val d: Decoder[WrongData.type] = c =>
      c.value
        .asString
        .filter(_ == "WrongData")
        .as(WrongData)
        .toRight(DecodingFailure("err", List.empty))
  }

  type R = Ok :+: Error :+: QuotaExceeded.type :+: WrongData.type :+: CNil
  object R {
    def apply[A](t: A)(implicit inj: Inject[R, A]): R = inj(t)
  }

  def calculate(x: Int): R = x match {
    case 1 => R(Ok(UUID.randomUUID()))
    case 2 => R(Error("something went wrong"))
    case 3 => R(QuotaExceeded)
    case _ => R(WrongData)
  }

  pprint.pprintln(calculate(1)) // Inl(head = Ok(token = 27f6ec66-3ec8-4b28-a0ef-c7289d0a6270))
  pprint.pprintln(calculate(2)) // Inr(tail = Inl(head = Error(message = "something went wrong")))
  pprint.pprintln(calculate(3)) // Inr(tail = Inr(tail = Inl(head = QuotaExceeded)))
  pprint.pprintln(calculate(4)) // Inr(tail = Inr(tail = Inr(tail = Inl(head = WrongData))))

  pprint.pprintln(Ok.next)
  pprint.pprintln(Ok.next.asJson.noSpaces)
  pprint.pprintln(WrongData.asJson.noSpaces)

  val x1 = io.circe.parser.decode[WrongData.type]("whatever")
  val x2 = io.circe.parser.decode[WrongData.type]("\"WrongData\"")
  pprint.pprintln(x1)
  pprint.pprintln(x2)
}
