package t20211602

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object App2 extends App {

  def f2 = 12
  
}

class App2Spec extends AnyFunSpec with Matchers {

  describe("a") {
    it("1") {
      App2.f2 shouldEqual 12
    }
  }
}
