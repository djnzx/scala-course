package glovo.q

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskBSpec extends AnyFunSpec with Matchers {
  describe("Task B") {
    import TaskBData._
    
    it("Java Implementation") {
      val app = new TaskBJava
      app.solution(INPUT.map(_.clone)) shouldEqual EXPECTED
    }
    
    it("Scala Iterative Implementation") {
      val app = new TaskBScalaIterative
      app.solution(INPUT.map(_.clone)) shouldEqual EXPECTED
    }
    
    it("Scala Tail Recursive Implementation") {
      val app = new TaskBScalaITailRec
      app.solution(INPUT.map(_.clone)) shouldEqual EXPECTED
    }
    
  }
}
