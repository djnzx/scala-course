package fp_red.red09.p9eval

import fp_red.red09.algebra.MonoPolyNom.Polynom
import fp_red.red09.p1impl.Reference
import fp_red.red09.p2concrete.math_polynom.MathOpToPolynomParser
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MathPolynomEvalSpec extends AnyFunSpec with Matchers {
  val R = Reference
  import MathPolynomEval.eval
  import MathOpToPolynomParser.built

  implicit class StrNorm(s: String) {
    def ws: String = s.replaceAll("\\s", "")
  }

  describe("eval:polynom:easy") {

    it("1") {
      val raw = "5x^2"
      R.run(built)(raw).map { eval } shouldBe Right(new Polynom(5, 2))
    }

    it("2") {
      val raw = "5x^2+3x^2"
      R.run(built)(raw).map { eval } shouldBe Right(new Polynom(8, 2))
    }

    it("3") {
      val raw = "5x^2+3x^6"
      R.run(built)(raw).map { eval } shouldBe Right(Polynom.of((3, 6), (5, 2)))
    }

    it("4") {
      val raw = "(5x+1)*(3x^6+2x^4)"
      R.run(built)(raw).map { eval } shouldBe Right(Polynom.of((15, 7), (3, 6), (10, 5), (2, 4)))
    }

  }
  
  describe("eval:polynom:real") {
    it("1") {
      val raw = "10x + 2x - (3x + 6)/3"
      val exp = Polynom.of((11, 1), (-2, 0))
      val sr = "11x - 2"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
      R.run(built)(raw.ws).map { eval } map { _.toStringHR } fold (_ => ???, identity) shouldBe sr
    }

    it("2") {
      val raw = "18*(2x+2) - 5  "
      val exp = Polynom.of((36, 1), (31, 0))
      val sr = "36x + 31"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
    }

    it("3") {
      val raw = "((9x + 81)/3 + 27)/3  - 2x  "
      val exp = Polynom.of((-1, 1), (18, 0))
      val sr = "-x + 18"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
    }

    it("4") {
      val raw = "18x + (12x + 10)*(2x+4)/2 - 5x  "
      val exp = Polynom.of((12, 2), (47, 1), (20, 0))
      val sr = "12x^2 + 47x + 20"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
    }

    it("5") {
      val raw = "(2x+5) * (x*(9x + 81)/3 + 27)/(1+1+1)  - 2x  "
      val exp = Polynom.of((2, 3), (23, 2), (61, 1), (45, 0))
      val sr = "2x^3 + 23x^2 + 61x + 45"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
    }

    it("6") {
      val raw = "(2x+5) * (x*(9x^3 + 81)/3 + 27)/(1+1+1)  - 2x  "
      val exp = Polynom.of((2, 5), (5, 4), (18, 2), (61, 1), (45, 0))
      val sr = "2x^5 + 5x^4 + 18x^2 + 61x + 45"
      R.run(built)(raw.ws).map { eval } shouldBe Right(exp)
    }

  }
  
}
