package glovo.q

import tools.spec.ASpec

/**
  * Binomial coefficient
  * 
  *               N !         N (N - 1) ... (K + 1)
  * C(N, K) = ------------- = ---------------------
  *           (N - K)! * K!           (N - K) !
  *
  * another solution [[geeksforgeeks.p1basic.B005BinomialCoefficient]]
  */
object TaskA {

  type BD = java.math.BigDecimal
  val M1 = new BD(1_000_000_000)
  val ONE = new BD(1)
  val ERR = -1
  
  def mul(a: BD, b: Int) = a.multiply(new BD(b))
  def fact(n: Int, r: BD = ONE): BD = if (n == 0) r else fact(n - 1, mul(r, n))
  def nUntilK(n: Int, k: Int) = ((k + 1) to n).foldLeft(ONE)(mul)
  def value(n: Int, k: Int) = nUntilK(n, k).divide(fact(n-k))

  def isValidInput(n: Int, k: Int) = !(n < 0 || k < 0 || n < k)
  def isValidResult(n: BD) = n.compareTo(M1) <= 0

  def solution(n: Int, k: Int) =
    if (!isValidInput(n, k)) ERR
    else Some(value(n, k))
      .filter(isValidResult)
      .map(_.intValue)
      .getOrElse(ERR)

}

class TaskASpec extends ASpec {
  import TaskA._
  import geeksforgeeks.p1basic.B005BinomialCoefficientData._

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
        nUntilK(5, 3) shouldEqual new BD(20)
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

  it("solution") {
    runAllD(data, (solution _).tupled)
  }
  
}
