package hackerrankfp.using

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class UsingSpec extends AnyFunSpec with Matchers {
  import UsingEx._
  
  describe("one line file reading with mapping and error handling") {
    
    it("1:one line file reading") {
      "1.json".toLine() shouldBe """{ "a": 1 }"""
    }
    
    it("2:multiline file reading") {
      "2.json".toLine() shouldBe
        """{
          |  "a": 5,
          |  "b": 123
          |}""".stripMargin
    }
    
    it("3:multiline file reading with extra mapping") {
      "2.json".toLine(_.trim) shouldBe
        """{
          |"a": 5,
          |"b": 123
          |}""".stripMargin
    }

    it("4:missed file w/o exception handling") {
      a [NotImplementedError] should be thrownBy "3.json".toLine()
    }
    
    it("5:missed file exception handling") {
      "3.json".toLine(handler = _ => ">>> NO FILE !!!") shouldBe
        ">>> NO FILE !!!"
    }
  }

}
