package rtj.auth

import io.estatico.newtype.macros.newtype
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import AuthDomain.Password
import AuthDomain.UserName
import io.circe.generic.semiauto._
import io.circe.syntax.EncoderOps
import io.circe._
import io.circe.parser._

import java.util.UUID

object AuthDomain {
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)
}

trait AuthService {
  def auth(user: UserName, pass: Password): Either[String, UUID]
}

class AuthServiceInMemory extends AuthService {

  private val data = Map(
    UserName("jim")   -> Password("123"),
    UserName("alex")  -> Password("1"),
    UserName("admin") -> Password("2"),
  )

  override def auth(user: UserName, pass: Password): Either[String, UUID] =
    data.get(user).contains(pass) match {
      case true  => Right(UUID.randomUUID())
      case false => Left("Wrong user/pass combination")
    }
}

object AuthService {
  def apply(): AuthService = new AuthServiceInMemory
}

class AuthServiceInMemoryTest extends AnyFunSpec with Matchers {

  val as = AuthService.apply()

  describe("auth") {
    it("should success") {
      val ar = as.auth(UserName("jim"), Password("123"))
      ar.isRight shouldEqual true
      ar.foreach(println)
    }

    it("should fail") {
      as.auth(UserName("jim"), Password("1234")) shouldEqual Left("Wrong user/pass combination")
    }
  }

}
