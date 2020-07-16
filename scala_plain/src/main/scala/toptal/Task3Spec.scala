package toptal

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Task3Spec extends AnyFunSpec with Matchers {
  
  import Task3._
  
  describe("task3") {
    it("case 1") {
      task() shouldEqual 3
    }
  }
  
}
