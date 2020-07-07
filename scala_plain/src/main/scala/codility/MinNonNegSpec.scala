package codility

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MinNonNegSpec extends AnyFunSpec with Matchers {
  describe("minNonNeg") {
    import  Codility.minNonNeg

    it("1") {
      minNonNeg(Array()) shouldEqual 1 
    }
    it("2") {
      minNonNeg(Array(1,2,3)) shouldEqual 4 
    }
    it("3") {
      minNonNeg(Array(1,3,4)) shouldEqual 2 
    }
    it("4") {
      minNonNeg(Array(-1,-2,-3)) shouldEqual 1 
    }
  }
}
