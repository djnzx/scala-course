package shapelss.coproduct

import cats.implicits.toFunctorOps
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.HCursor
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.:+:
import shapeless.CNil
import shapeless.ops.coproduct.Inject

import java.util.UUID

object CoproductLearning {

  /** representation */
  case class Ok(token: UUID)
  object Ok extends AutoDerivation {
    def next = Ok(UUID.randomUUID())
  }
  case class Error(message: String)
  object Error extends AutoDerivation
  case object QuotaExceeded extends AutoDerivation
  case object WrongData {
    implicit val e: Encoder[WrongData.type] = _ => "WrongData".asJson
    implicit val d: Decoder[WrongData.type] = (c: HCursor) =>
      c.value
        .asString
        .filter(_ == "WrongData")
        .as(WrongData)
        .toRight(DecodingFailure("err", List.empty))
  }

  /** composite type */
  type R = Ok :+: Error :+: QuotaExceeded.type :+: WrongData.type :+: CNil

  /** type builder */
  object R {

    /** manual lifter */
    def apply[A](t: A)(implicit inj: Inject[R, A]): R = inj(t)

    /** auto lifter for everything can be lifted */
    implicit def autoLiftMembers[A](a: A)(implicit inj: Inject[R, A]): R = inj(a)
  }

  def liftManually(x: Int): R = x match {
    case 1 => R(Ok(UUID.randomUUID()))
    case 2 => R(Error("something went wrong"))
    case 3 => R(QuotaExceeded)
    case _ => R(WrongData)
  }

}

class CoproductSpec extends AnyFunSpec with Matchers {
  import shapelss.coproduct.CoproductLearning.Error
  import shapelss.coproduct.CoproductLearning.Ok
  import shapelss.coproduct.CoproductLearning.QuotaExceeded
  import shapelss.coproduct.CoproductLearning.R
  import shapelss.coproduct.CoproductLearning.WrongData
  import shapelss.coproduct.CoproductLearning.liftManually

  describe("decoding") {
    it("decoding success") {
      io.circe.parser.decode[WrongData.type]("\"WrongData\"") shouldEqual Right(WrongData)
    }

    it("decoding failure") {
      io.circe.parser.decode[WrongData.type]("whatever").isRight shouldBe false
    }
  }

  describe("encoding") {
    pprint.pprintln(Ok.next) // Ok(token = 3db265be-4f90-466c-a0c4-92274f417cc7)
    pprint.pprintln(Ok.next.asJson.noSpaces) // "{\"token\":\"8b3be058-339e-4499-9adf-a6d3763830e5\"}"
    pprint.pprintln(WrongData.asJson.noSpaces) // "\"WrongData\""
  }

  describe("lift manually") {
    val r1: R = liftManually(1)
    val r2: R = liftManually(2)
    val r3: R = liftManually(3)
    val r4: R = liftManually(4)
    pprint.pprintln(r1) // Inl(head = Ok(token = 27f6ec66-3ec8-4b28-a0ef-c7289d0a6270))
    pprint.pprintln(r2) // Inr(tail = Inl(head = Error(message = "something went wrong")))
    pprint.pprintln(r3) // Inr(tail = Inr(tail = Inl(head = QuotaExceeded)))
    pprint.pprintln(r4) // Inr(tail = Inr(tail = Inr(tail = Inl(head = WrongData))))
  }

  describe("lift automatically") {
    import shapelss.coproduct.CoproductLearning.R.autoLiftMembers

    def x(r: R) = r

    x(Error("message"))
    x(WrongData)
    x(QuotaExceeded)
    x(Ok.next)
//    x(1)
  }

}
