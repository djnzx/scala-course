package glovolive

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.Console._

class TaskBSpec extends AnyFunSpec with Matchers {
  describe("Task B") {
    it("isValid") {
      import 
//      TaskBJava1._
//      TaskBJava2._
      TaskBScala._

      val t = List(
        "",
        "()",
        "[]",
        "{}",
        "()[]",
        "({}){[]}",
        "([{}])",
      )
      val f = List(
        " ",
        "(",
        "][",
        "[)",
        "{[}]",
        "[{[}]()]",
      )
      
      (t.map(_ -> true) ++ f.map(_ -> false))
        .toMap
        .foreach { case (s, exp) =>
          println(s"going to test: $GREEN$s ~> $exp$RESET")
          isValid(s) shouldEqual exp
      }
    }
  }
}
