package toptal

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task1Spec extends AnyFunSpec with Matchers {
  
  import Task1._
  
  describe("task1") {
    it("case 1") {
//      count_uniques(Map()) shouldEqual 0
    }
    it("case 2") {
//      count_uniques(Map('a'->1)) shouldEqual 1
    }
    it("case 3") {
//      count_uniques(Map('a'->1, 'b'->2)) shouldEqual 1
    }
    it("case 4") {
//      count_uniques(Map('a'->1, 'b'->1)) shouldEqual 2
    }
  }
  
}
