package toptal1

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task0Spec extends AnyFunSpec with Matchers {
  
  import Task0._
  
  describe("task0") {
    it("1") {
      minNonNeg(Array()) shouldEqual 1
    }
    it("2") {
      minNonNeg(Array(-1,-2)) shouldEqual 1
    }
    it("3") {
      minNonNeg(Array(1,2)) shouldEqual 3
    }
    it("4") {
      minNonNeg(Array(1,2,4)) shouldEqual 3
    }
  }
  
}
