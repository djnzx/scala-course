package toptal

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task2Spec extends AnyFunSpec with Matchers {
  
  import Task2._
  
  describe("task2") {
    it("1") {
      stringToTuples("") shouldBe List()
    }
    it("2") {
      stringToTuples("A") shouldBe List('A'->1) 
    }
    it("3") {
      stringToTuples("AB") shouldBe List('A'->1, 'B'->1) 
    }
    it("4") {
      stringToTuples("ABB") shouldBe List('A'->1, 'B'->2) 
    }
    it("5") {
      stringToTuples("ABBC") shouldBe List('A'->1, 'B'->2, 'C'->1) 
    }
  }
  
}
