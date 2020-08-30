package request

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import request.Endpoints.urlLogin
import request.dto.LoginRq
import requests.Response

class LoginSpec extends AnyFunSpec with Matchers {
  describe("login") {
    it("POST") {
      val headers = Seq(
        "Content-Type"->"application/json",
      )
      val email = "a77x77@gmail.com"
      val password = "abc123DEF"
      
      val loginRq = LoginRq(email, password)
      val json = upickle.default.write(loginRq)
      val r: Response = requests.post(urlLogin, headers = headers, data = json)
      r.statusCode shouldEqual 200
      pprint.pprintln(r.data)
    }
    
  }
}
