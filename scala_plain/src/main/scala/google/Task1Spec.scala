package google

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task1Spec extends AnyFunSpec with Matchers {

  describe("task1") {
    import Task1._
    
    it("a") {
      add(1, 10) shouldEqual 11
    }
  }
  
}
