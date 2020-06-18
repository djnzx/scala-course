package fp_red.red09.p2concrete.math_monom

import fp_red.red09.algebra.MonoPolyNom.Monom
import fp_red.red09.p1impl.Reference
import fp_red.red09.p2concrete.math_monom.MathOpToMonomParser.{BiOp, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AnyFunSpec

import scala.language.implicitConversions

// 11
class MathOpToMonomSpec extends AnyFunSpec with Matchers {
  val R = Reference
  import MathOpToMonomParser.mathToMonom

  implicit class StrNorm(s: String) {
    def ws: String = s.replaceAll("\\s", "")
  }
  
  describe("plain (w/o operations)") {
    it("1: x") {
      val r = R.runLen(mathToMonom)(" x ".ws)
      pprint.log(r)
    }
    it("2: 5x") {
      R.runLen(mathToMonom)(" 5x ".ws) shouldBe
        Right((Value(Monom(5, 1)), 2))
    }
    it("3: x^3") {
      R.runLen(mathToMonom)("x^3".ws) shouldBe
        Right((Value(Monom(1, 3)), 3))
    }
    it("4: 4x^3") {
      R.runLen(mathToMonom)("4x^3".ws) shouldBe
        Right((Value(Monom(4, 3)), 4))
    }
    it("4: 5") {
      R.runLen(mathToMonom)("5".ws) shouldBe
        Right((Value(Monom(5, 0)), 1))
    }
  }

  describe("nx^p: real cases") {
    it("1") {
      R.runLen(mathToMonom)("18*(2x+2)-5".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp('*', Value(Monom(18, 0)), BiOp('+', Value(Monom(2, 1)), Value(Monom(2, 0)))),
              Value(Monom(5, 0))
            ),
            11
        ))
    }
    
    it("2") {
      R.runLen(mathToMonom)("10x + 2x - (3x + 6)/3".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp('+', Value(Monom(10, 1)), Value(Monom(2, 1))),
              BiOp('/', BiOp('+', Value(Monom(3, 1)), Value(Monom(6, 0))), Value(Monom(3, 0)))
            ),
            15
        ))
    }
    
    it("3") {
      R.run(mathToMonom)("((9x + 81)/3 + 27)/3  - 2x".ws) shouldBe
        Right(
          BiOp(
            '-',
            BiOp(
              '/',
              BiOp(
                '+',
                BiOp('/', BiOp('+', Value(Monom(9, 1)), Value(Monom(81, 0))), Value(Monom(3, 0))),
                Value(Monom(27, 0))
              ),
              Value(Monom(3, 0))
            ),
            Value(Monom(2, 1))
          )
        )
    }
    
    it("4") {
      R.runLen(mathToMonom)("18x + (12x + 10)*(2x+4)/2 - 5x".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp(
                '+',
                Value(Monom(18, 1)),
                BiOp(
                  '/',
                  BiOp(
                    '*',
                    BiOp('+', Value(Monom(12, 1)), Value(Monom(10, 0))),
                    BiOp('+', Value(Monom(2, 1)), Value(Monom(4, 0)))
                  ),
                  Value(Monom(2, 0))
                )
              ),
              Value(Monom(5, 1))
            ),
            24
        ))
    }
    
    it("5") {
      R.runLen(mathToMonom)("(2x+5) * (x*(9x + 81)/3 + 27)/(1+1+1)  - 2x".ws) shouldBe
        Right(
          (
            BiOp(
              '-',
              BiOp(
                '/',
                BiOp(
                  '*',
                  BiOp('+', Value(Monom(2, 1)), Value(Monom(5, 0))),
                  BiOp(
                    '+',
                    BiOp(
                      '/',
                      BiOp('*', Value(Monom(1, 1)), BiOp('+', Value(Monom(9, 1)), Value(Monom(81, 0)))),
                      Value(Monom(3, 0))
                    ),
                    Value(Monom(27, 0))
                  )
                ),
                BiOp('+', BiOp('+', Value(Monom(1, 0)), Value(Monom(1, 0))), Value(Monom(1, 0)))
              ),
              Value(Monom(2, 1))
            ),
            34
          )
        )
    }
    
    it("6") {
      R.runLen(mathToMonom)("(2x+5) * (x*(9x^3 + 81)/3 + 27)/(1+1+1)  - 2x  ".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp(
                '/',
                BiOp(
                  '*',
                  BiOp('+', Value(Monom(2, 1)), Value(Monom(5, 0))),
                  BiOp(
                    '+',
                    BiOp(
                      '/',
                      BiOp('*', Value(Monom(1, 1)), BiOp('+', Value(Monom(9, 3)), Value(Monom(81, 0)))),
                      Value(Monom(3, 0))
                    ),
                    Value(Monom(27, 0))
                  )
                ),
                BiOp('+', BiOp('+', Value(Monom(1, 0)), Value(Monom(1, 0))), Value(Monom(1, 0)))
              ),
              Value(Monom(2, 1))
            ),
            36
        ))
    }

  }

}
