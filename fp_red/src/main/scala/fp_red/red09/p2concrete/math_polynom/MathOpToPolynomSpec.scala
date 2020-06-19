package fp_red.red09.p2concrete.math_polynom

import fp_red.red09.algebra.MonoPolyNom.Polynom
import fp_red.red09.p1impl.Reference
import fp_red.red09.p2concrete.math.BiTree.{BiOp, Value}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.language.implicitConversions

//
class MathOpToPolynomSpec extends AnyFunSpec with Matchers {
  val R = Reference
  import fp_red.red09.p2concrete.math_polynom.MathOpToPolynomParser.built

  implicit class StrNorm(s: String) {
    def ws: String = s.replaceAll("\\s", "")
  }
  
  describe("plain (w/o operations)") {
    it("1: x") {
      R.run(built)(" x ".ws) shouldBe
        Right(Value(new Polynom(1, 1)))
    }
    it("2: 5x") {
      R.runLen(built)(" 5x ".ws) shouldBe
        Right((Value(new Polynom(5, 1)), 2))
    }
    it("3: x^3") {
      R.runLen(built)("x^3".ws) shouldBe
        Right((Value(new Polynom(1, 3)), 3))
    }
    it("4: 4x^3") {
      R.runLen(built)("4x^3".ws) shouldBe
        Right((Value(new Polynom(4, 3)), 4))
    }
    it("4: 5") {
      R.runLen(built)("5".ws) shouldBe
        Right((Value(new Polynom(5, 0)), 1))
    }
  }

  describe("nx^p: real cases") {
    it("1") {
      R.runLen(built)("18*(2x+2)-5".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp('*', Value(new Polynom(18, 0)), BiOp('+', Value(new Polynom(2, 1)), Value(new Polynom(2, 0)))),
              Value(new Polynom(5, 0))
            ),
            11
        ))
    }
    
    it("2") {
      R.runLen(built)("10x + 2x - (3x + 6)/3".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp('+', Value(new Polynom(10, 1)), Value(new Polynom(2, 1))),
              BiOp('/', BiOp('+', Value(new Polynom(3, 1)), Value(new Polynom(6, 0))), Value(new Polynom(3, 0)))
            ),
            15
        ))
    }
    
    it("3") {
      R.run(built)("((9x + 81)/3 + 27)/3  - 2x".ws) shouldBe
        Right(
          BiOp(
            '-',
            BiOp(
              '/',
              BiOp(
                '+',
                BiOp('/', BiOp('+', Value(new Polynom(9, 1)), Value(new Polynom(81, 0))), Value(new Polynom(3, 0))),
                Value(new Polynom(27, 0))
              ),
              Value(new Polynom(3, 0))
            ),
            Value(new Polynom(2, 1))
          )
        )
    }
    
    it("4") {
      R.runLen(built)("18x + (12x + 10)*(2x+4)/2 - 5x".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp(
                '+',
                Value(new Polynom(18, 1)),
                BiOp(
                  '/',
                  BiOp(
                    '*',
                    BiOp('+', Value(new Polynom(12, 1)), Value(new Polynom(10, 0))),
                    BiOp('+', Value(new Polynom(2, 1)), Value(new Polynom(4, 0)))
                  ),
                  Value(new Polynom(2, 0))
                )
              ),
              Value(new Polynom(5, 1))
            ),
            24
        ))
    }
    
    it("5") {
      R.runLen(built)("(2x+5) * (x*(9x + 81)/3 + 27)/(1+1+1)  - 2x".ws) shouldBe
        Right(
          (
            BiOp(
              '-',
              BiOp(
                '/',
                BiOp(
                  '*',
                  BiOp('+', Value(new Polynom(2, 1)), Value(new Polynom(5, 0))),
                  BiOp(
                    '+',
                    BiOp(
                      '/',
                      BiOp('*', Value(new Polynom(1, 1)), BiOp('+', Value(new Polynom(9, 1)), Value(new Polynom(81, 0)))),
                      Value(new Polynom(3, 0))
                    ),
                    Value(new Polynom(27, 0))
                  )
                ),
                BiOp('+', BiOp('+', Value(new Polynom(1, 0)), Value(new Polynom(1, 0))), Value(new Polynom(1, 0)))
              ),
              Value(new Polynom(2, 1))
            ),
            34
          )
        )
    }
    
    it("6") {
      R.runLen(built)("(2x+5) * (x*(9x^3 + 81)/3 + 27)/(1+1+1)  - 2x  ".ws) shouldBe
        Right((
            BiOp(
              '-',
              BiOp(
                '/',
                BiOp(
                  '*',
                  BiOp('+', Value(new Polynom(2, 1)), Value(new Polynom(5, 0))),
                  BiOp(
                    '+',
                    BiOp(
                      '/',
                      BiOp('*', Value(new Polynom(1, 1)), BiOp('+', Value(new Polynom(9, 3)), Value(new Polynom(81, 0)))),
                      Value(new Polynom(3, 0))
                    ),
                    Value(new Polynom(27, 0))
                  )
                ),
                BiOp('+', BiOp('+', Value(new Polynom(1, 0)), Value(new Polynom(1, 0))), Value(new Polynom(1, 0)))
              ),
              Value(new Polynom(2, 1))
            ),
            36
        ))
    }

  }

}
