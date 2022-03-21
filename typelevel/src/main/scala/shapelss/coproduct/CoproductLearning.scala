package shapelss.coproduct

import cats.implicits.toFunctorOps
import io.circe
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.HCursor
import io.circe.generic.AutoDerivation
import io.circe.shapes._
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.:+:
import shapeless.CNil
import shapeless.Coproduct
import shapeless.ops.coproduct.Inject

import java.util.UUID

object CoproductLearning {

  /** representation, all encoders, decoders packed respectively */
  case class Ok(token: UUID)
  object Ok extends AutoDerivation {
    def next = Ok(UUID.randomUUID())
  }
  case class Error(message: String)
  object Error extends AutoDerivation
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
  type R = Ok :+: Error :+: WrongData.type :+: CNil

  /** type builder */
  object R {

    /** manual lifter */
    def apply[A](t: A)(implicit inj: Inject[R, A]): R = inj(t)

    /** auto lifter for everything can be lifted */
    implicit def autoLiftMembers[A](a: A)(implicit inj: Inject[R, A]): R = inj(a)

    /** manual encoding */
//    implicit val e: Encoder[R] = {
//      case Inl(ok)                  => ok.asJson
//      case Inr(Inl(error))          => error.asJson
//      case Inr(Inr(Inl(wrongData))) => wrongData.asJson
//      case _                        => JsNull.asInstanceOf[Json]
//    }
  }

  def liftManually(x: Int): R = x match {
    case 1 => R(Ok(UUID.randomUUID()))
    case 2 => R(Error("something went wrong"))
    case _ => R(WrongData)
  }

}

class CoproductLearningSpec extends AnyFunSpec with Matchers {
  import shapelss.coproduct.CoproductLearning.Error
  import shapelss.coproduct.CoproductLearning.Ok
  import shapelss.coproduct.CoproductLearning.R
  import shapelss.coproduct.CoproductLearning.WrongData
  import shapelss.coproduct.CoproductLearning.liftManually

  it("encoding / serialization") {
    pprint.pprintln(WrongData.asJson.noSpaces)
    pprint.pprintln(Error("123").asJson.noSpaces)
    pprint.pprintln(Ok.next) // Ok(token = 3db265be-4f90-466c-a0c4-92274f417cc7)
    pprint.pprintln(Ok.next.asJson.noSpaces) // "{\"token\":\"8b3be058-339e-4499-9adf-a6d3763830e5\"}"
    pprint.pprintln(WrongData.asJson.noSpaces) // "\"WrongData\""

    val r1: R = R(Ok.next)
    println(r1.asJson)
  }

  it("decoding") {
    io.circe.parser.decode[WrongData.type]("\"WrongData\"") shouldEqual Right(WrongData)
    io.circe.parser.decode[WrongData.type]("whatever").isRight shouldBe false
  }

  it("lift manually") {
    val r1: R = liftManually(1)
    val r2: R = liftManually(2)
    val r3: R = liftManually(3)
    pprint.pprintln(r1) // Inl(head = Ok(token = 27f6ec66-3ec8-4b28-a0ef-c7289d0a6270))
    pprint.pprintln(r2) // Inr(tail = Inl(head = Error(message = "something went wrong")))
    pprint.pprintln(r3) // Inr(tail = Inr(tail = Inl(head = WrongData)))
  }

  it("lift automatically") {
    import shapelss.coproduct.CoproductLearning.R.autoLiftMembers

    def x(r: R) = r

    x(Error("message"))
    x(WrongData)
    x(Ok.next)
//    x(1)
  }

  it("derived encoder") {
    val r1: R = Coproduct[R](Ok.next)
    pprint.pprintln(r1.asJson.noSpaces)
  }

  it("derived decoder") {
    val r: Either[circe.Error, R] = io.circe.jawn.decode[R]("{\"token\":\"0624bdaa-869e-4a67-9430-07b8511fd369\"}")
    r shouldEqual Right(Coproduct[R](Ok(UUID.fromString("0624bdaa-869e-4a67-9430-07b8511fd369"))))
    println("---------")
    println(r)
    println("---------")
  }
}
