package glovolive

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskBSpec extends AnyFunSpec with Matchers {
  describe("task B") {
    it("isValid") {
      import TaskBJava1._

      val trueItems = List(
        "",
        "()",
        "[]",
        "{}",
        "()[]",
        "({}){[]}",
        "([{}])",
      )
      val falseItems = List(
        "(",
        "][",
        "[)",
        "{[}]",
        "[{[}]()]",
      )
      
      (trueItems.map(_ -> true) ++ falseItems.map(_ -> false))
        .toMap
        .foreach{ case (s, exp) =>
        isValid(s) shouldEqual exp
      }
    }
  }
}
