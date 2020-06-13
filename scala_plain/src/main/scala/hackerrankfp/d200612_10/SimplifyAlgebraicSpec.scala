package hackerrankfp.d200612_10

import hackerrankfp.d200612_10.SimplifyAlgebraicExpressions.{Monom, Polynom}
import org.scalatest._

import scala.collection.mutable

class SimplifyAlgebraicSpec extends funspec.AnyFunSpec {
  describe("Monom") {
    
    describe("Representation") {
      it("mkString: don't add the plus at the beginning") {
        mutable.LinkedHashMap(
          Monom(5,2) -> "5x^2",
          Monom(5,1) -> "5x",
          Monom(5,0) -> "5",
          Monom(-3,2) -> "-3x^2",
          Monom(-3,1) -> "-3x",
          Monom(-3,0) -> "-3",
          Monom(-1,2) -> "-x^2",
          Monom(-1,1) -> "-x",
          Monom(1,1) -> "x",
          Monom(0,0) -> "",
          Monom(0,1) -> "",
        )
          .foreach { case (m, r) => assert (m.mkString == r)}
      }
      it("toString: ADD the plus at the beginning") {
        mutable.LinkedHashMap(
          Monom(5,2) -> "+5x^2",
          Monom(5,1) -> "+5x",
          Monom(5,0) -> "+5",
          Monom(-3,2) -> "-3x^2",
          Monom(-3,1) -> "-3x",
          Monom(-3,0) -> "-3",
          Monom(-1,2) -> "-x^2",
          Monom(-1,1) -> "-x",
          Monom(1,1) -> "+x",
          Monom(0,0) -> "",
          Monom(0,1) -> "",
        )
          .foreach { case (m, r) => assert (m.toString == r)}
      }
    }
    
    describe("Operations") {
      it("isNeg") {
        assert(  Monom(-2, 3).isNeg)
        assert(! Monom( 0, 3).isNeg)
        assert(! Monom( 5, 3).isNeg)
      }
      it("isZero") {
        assert(  Monom( 0, 3).isZero)
        assert(! Monom(-1, 3).isZero)
        assert(! Monom( 5, 3).isZero)
      }
      it("unary -") {
        assert(- Monom(2, 3) == Monom(-2, 3))
        assert(- Monom(0, 3) == Monom( 0, 3))
      }
      it("+: squash the same power to one item") {
        assert(Monom(2,3) + Monom(3,3) == Polynom.of((5, 3)))
      }
      it("+: the same power and mirrored K should disappear") {
        assert(Monom(-3,3) + Monom(3,3) == Polynom.of())
      }
      it("+: after adding sort in right order") {
        assert(Monom(5,2) + Monom(3,3) == Polynom.of((3, 3), (5, 2)))
      }
      it("*: just multiply K and add P") {
        assert(Monom(3,3) * Monom(5,2) == Monom(15,5))
        assert(Monom(0,3) * Monom(5,2) == Monom(0,5))
      }
      it("/: divide without remainder") {
        assert(Monom(6,3) / 2 == Monom(3,3))
        assert(Monom(7,3) / 2 == Monom(3,3))
        assert(Monom(1,3) / 2 == Monom(0,3))
      }
      it("toPolynom") {
        val m = Monom(6,3)
        assert(m.toPolynom == Polynom(Seq(m)))
      }
    }
  }

  describe("Polynom") {
    val p1 = Polynom.of((-12,3), (4,1), (-3,2), (-5,0))
    val p2 = Polynom.of(( 12,3), (4,1), (-3,2), (-5,0))
    
    describe("Representation") {
      it("should be represented in given order") {
        assert(p1.toString == "-12x^3+4x-3x^2-5")
        assert(p2.toString == "12x^3+4x-3x^2-5")
      }
    }
    
    describe("Operations") {
      it("5") {
        assert(Set.empty.size == 0)
      }
    }
  }
}
