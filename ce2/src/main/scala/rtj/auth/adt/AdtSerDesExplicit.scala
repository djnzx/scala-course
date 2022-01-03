package rtj.auth.adt

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class AdtSerDesExplicit extends AnyFunSpec with Matchers {

  sealed trait AuthResult
  case class AuthSuccess(token: UUID) extends AuthResult
  object AuthSuccess {
    implicit val encoder: Encoder[AuthSuccess] = deriveEncoder
    implicit val decoder: Decoder[AuthSuccess] = deriveDecoder
  }
  case class AuthFailed(message: String) extends AuthResult
  object AuthFailed {
    implicit val encoder: Encoder[AuthFailed] = deriveEncoder
    implicit val decoder: Decoder[AuthFailed] = deriveDecoder
  }

  object AuthResult {
    import cats.implicits._

    // explicit encoder
    implicit val encoder: Encoder[AuthResult] = Encoder.instance {
      case x: AuthSuccess => x.asJson
      case x: AuthFailed  => x.asJson
    }
    // explicit decoder. actually, manually try to use all decoders
    implicit val decoder: Decoder[AuthResult] = List[Decoder[AuthResult]](
      Decoder[AuthSuccess].widen,
      Decoder[AuthFailed].widen,
    ).reduceLeft(_ or _)
  }

  val uuid: UUID = UUID.fromString("c994fffd-2e61-4eb3-8876-af4367867070")
  val msg = "wrong combination"

  val arSucc: AuthResult = AuthSuccess(uuid)
  val arFail: AuthResult = AuthFailed(msg)

  val rawSuccess: String =
    s"""{
      |  "token" : "$uuid"
      |}""".stripMargin
  val rawFailed: String =
    s"""{
      |  "message" : "$msg"
      |}""".stripMargin
  val rawWrongSyntax: String = "whatever"
  val rawWrongInstance: String =
    """
      |{
      |  "code" : true
      |}
      |""".stripMargin

  describe("serialization") {
    it("success") {
      arSucc.asJson.spaces2 shouldEqual rawSuccess
    }
    it("failure") {
      arFail.asJson.spaces2 shouldEqual rawFailed
    }
  }

  describe("deSerialization") {
    import io.circe.Error
    import io.circe.DecodingFailure
    import org.typelevel.jawn.ParseException
    import io.circe.ParsingFailure
    import io.circe.CursorOp.DownField

    it("success") {
      val r: Either[Error, AuthResult] = decode[AuthResult](rawSuccess)
      r shouldEqual Right(arSucc)
    }
    it("failure") {
      val r: Either[Error, AuthResult] = decode[AuthResult](rawFailed)
      r shouldEqual Right(arFail)
    }
    it("wrong instance") {
      val r: Either[Error, AuthResult] = decode[AuthResult](rawWrongInstance)
      r shouldEqual Left(
        DecodingFailure(
          "Attempt to decode value on failed cursor",
          List(DownField("message")), // no field with the name `message`
        ),
      )
    }
    it("wrong syntax") {
      val r: Either[Error, AuthResult] = decode[AuthResult](rawWrongSyntax)
      r shouldEqual Left(
        ParsingFailure(
          "expected json value got 'whatev...' (line 1, column 1)",
          ParseException(
            msg = "expected json value got 'whatev...' (line 1, column 1)",
            index = 0,
            line = 1,
            col = 1,
          ),
        ),
      )
    }

  }
}
