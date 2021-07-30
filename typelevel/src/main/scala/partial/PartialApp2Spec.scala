package partial

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PartialApp2Spec extends AnyFunSpec with Matchers {

  /**
    * basic tests for our
    * business the logic implementation
    */
  describe("basic business logic test") {
    import PartialApp2.businessLogic

    it("success cases") {
      businessLogic(1) shouldEqual "one"
      businessLogic(10) shouldEqual "ten"
    }

    it("exception can be thrown") {
      an[IllegalArgumentException] should be thrownBy businessLogic(2)
      (the [IllegalArgumentException] thrownBy businessLogic(2)).getMessage should include ("TWO")
    }

    it("can be partial (cover not all the cases)") {
      an [MatchError] should be thrownBy businessLogic(111)
      (the [MatchError] thrownBy businessLogic(111)).getMessage should equal ("111 (of class java.lang.Integer)")
    }

  }

}
