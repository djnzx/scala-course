package rtj_cats.auth.adt

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import java.util.UUID
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class AdtSerPitfall1 extends AnyFunSpec with Matchers {

  sealed trait AuthResult

  case class AuthSuccess(token: UUID) extends AuthResult

  object AuthSuccess {
    implicit val encoder1: Encoder[AuthSuccess] = deriveEncoder
  }

  object AuthResult {
    implicit val encoder2: Encoder[AuthResult] = deriveEncoder
  }

  describe("serialization") {

    val uuid: UUID = UUID.fromString("c994fffd-2e61-4eb3-8876-af4367867070")

    it("success... almost") {
      val arSucc1: AuthSuccess = AuthSuccess(uuid)
      val arSucc2: AuthResult = arSucc1

      val serialized1 = arSucc1.asJson.noSpaces // AuthSuccess serializer will be used
      val serialized2 = arSucc2.asJson.noSpaces // AuthResult serializer will be used - one extra level will be added

      serialized1 shouldEqual """{"token":"c994fffd-2e61-4eb3-8876-af4367867070"}"""
      serialized2 shouldEqual """{"AuthSuccess":{"token":"c994fffd-2e61-4eb3-8876-af4367867070"}}"""
    }
  }
}
