package t20211602

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object AirportLimousine extends App {

  def f1 = 11
  
}

class App1Spec extends AnyFunSpec with Matchers {
  
  describe("a") {
    it("1") {
      AirportLimousine.f1 shouldEqual 11
    }
  }
}
