package glovo

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskBSpec extends AnyFunSpec with Matchers {
  describe("Task B") {
    import TaskBData._
    
    it("Java Implementation") {
      val app = new TaskBJava()
      app.solution(INPUT) shouldEqual EXPECTED
    }
    
    it("Scala Implementation") {
      val app = new TaskBScala
      app.solution(INPUT) shouldEqual EXPECTED
    }
    
  }
}
