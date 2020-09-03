package glovo

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskASpec extends AnyFunSpec with Matchers {
  
  describe("NK") {
    import TaskA._
    
    describe("BigDecimal OPS") {
      it("ERR instance") {
        ERR shouldEqual -1
      }
      it("ONE instance") {
        ONE shouldEqual new BD(1)
      }
      it("m1 instance") {
        M1 shouldEqual new BD(1_000_000_000)
      }
      it("multiply operation") {
        mul(ONE, 12345) shouldEqual new BD(12345)
      }
    }

    describe("factorial") {
      it("a") {
        fact(0) shouldEqual ONE
      }
      it("b") {
        fact(1) shouldEqual ONE
      }
      it("c") {
        fact(5) shouldEqual new BD(120)
      }
    }

    describe("nUpToK") {
      it("a") {
        nUptoK(5, 3) shouldEqual new BD(20)
      }
    }

    describe("isValid") {
      it("true 1") {
        isValidResult(new BD(123_456_789)) shouldEqual true
      }
      it("true 2") {
        isValidResult(M1) shouldEqual true
      }
      it("false") {
        isValidResult(M1.add(ONE)) shouldEqual false
      }
    }

    describe("guards (-1)") {
      it("a") {
        solution(-3, 5) shouldEqual ERR
      }
      it("b") {
        solution(3, -5) shouldEqual ERR
      }
      it("c") {
        solution(-3, -5) shouldEqual ERR
      }
      it("d") {
        solution(3, 5) shouldEqual ERR
      }
    }

    describe("main composition") {
      it("a") {
        solution(5, 3) shouldEqual 10
      }

      it("b") {
        solution(40, 20) shouldEqual -1
      }
    }
  }
}
