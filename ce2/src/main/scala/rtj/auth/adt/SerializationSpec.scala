package rtj.auth.adt

import cats.implicits.toFunctorOps
import io.circe
import io.circe.Decoder
import io.circe.Encoder
import io.circe.Json
import io.circe.ParsingFailure
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.parser.decode
import io.circe.parser.parse
import io.circe.syntax._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class SerializationSpec extends AnyFunSpec with Matchers {

  sealed trait AuthResult
  case class AuthOk(token: UUID) extends AuthResult
  object AuthOk extends AutoDerivation
//  {
//    implicit val encoder: Encoder[AuthOk] = deriveEncoder
//    implicit val decoder: Decoder[AuthOk] = deriveDecoder
//  }
  case class AuthErr(message: String) extends AuthResult
  object AuthErr {
    implicit val encoder: Encoder[AuthErr] = deriveEncoder
    implicit val decoder: Decoder[AuthErr] = deriveDecoder
  }
  object AuthResult {
    val encoder: Encoder[AuthResult] = deriveEncoder

    // doesn't work with ADT
    val decoder0: Decoder[AuthResult] = deriveDecoder

    // approach1: explicit manual mapping, but the problem is - each time we manually try all of them
    implicit val decoder1: Decoder[AuthResult] = List[Decoder[AuthResult]](
      Decoder[AuthOk].widen,
      Decoder[AuthErr].widen,
    ).reduceLeft(_ or _)
  }
  val ok = AuthOk(UUID.randomUUID())
  val rawOk1 = """
                 |{
                 |  "token" : "c994fffd-2e61-4eb3-8876-af4367867070"
                 |}
                 |""".stripMargin

  describe("w/o discriminator, manual explicit") {

    it("serialize") {
      println(ok.asJson)
      println(AuthErr("wrong").asJson)
    }

    it("deserialize R - Ok") {
      val r1: Either[ParsingFailure, Json] = parse(rawOk1)
      pprint.pprintln(r1)
      val r2 = decode[AuthOk](rawOk1)
      pprint.pprintln(r2)
      val r3 = decode[AuthResult](rawOk1)
      pprint.pprintln(r3)
    }

    it("deserialize R - Err") {
      val raw = """
                  |{
                  |  "message" : "wrong"
                  |}
                  |""".stripMargin
      val r1: Either[ParsingFailure, Json] = parse(raw)
      pprint.pprintln(r1)
      val r2 = decode[AuthErr](raw)
      pprint.pprintln(r2)
      val r3 = decode[AuthResult](raw)
      pprint.pprintln(r3)
    }

    it("deserialize L - wrong") {
      val raw = """
                  |{
                  |  "code" : 13
                  |}
                  |""".stripMargin
      val r1: Either[ParsingFailure, Json] = parse(raw)
      pprint.pprintln(r1)
      val r3 = decode[AuthResult](raw)
      pprint.pprintln(r3)
    }

    it("deserialize L - wrong syntax") {
      val raw = """
                  |{
                  |  "code" : 1
                  |""".stripMargin
      val r1: Either[ParsingFailure, Json] = parse(raw)
      pprint.pprintln(r1)
    }

  }

  describe("with discriminator") {

    import io.circe.generic.extras.auto._
    implicit val genDevConfig: Configuration =
      Configuration.default.withDiscriminator("t")

    it("encode with discriminator") {
      val s1 = (ok: AuthResult).asJson.noSpaces
      pprint.pprintln(s1)
    }

    it("1") {
      val r1: Either[circe.Error, AuthResult] = decode[AuthResult](rawOk1)
      r1.foreach(println)
    }
  }

  describe("w/o discriminator, shapeless-based") {
    it("2") {

      object ShapesDerivation {

        import shapeless.Coproduct
        import shapeless.Generic

        implicit def encodeAdtNoDiscr[A, Repr <: Coproduct](
            implicit
            gen: Generic.Aux[A, Repr],
            encodeRepr: Encoder[Repr],
          ): Encoder[A] = encodeRepr.contramap(gen.to)

        implicit def decodeAdtNoDiscr[A, Repr <: Coproduct](
            implicit
            gen: Generic.Aux[A, Repr],
            decodeRepr: Decoder[Repr],
          ): Decoder[A] = decodeRepr.map(gen.from)

      }

      import io.circe.generic.extras.Configuration

      implicit val genDevConfig: Configuration =
        Configuration.default.withDiscriminator("t")
    }
  }

}
