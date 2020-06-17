package fp_red.red09.algebra

import fp_red.red09.algebra.SimplifyAlgebraicExpressions.{Monom, Polynom}
import org.scalatest._

import scala.collection.mutable

class SimplifyAlgebraicSpec extends funspec.AnyFunSpec
  with matchers.should.Matchers
  with OptionValues
  with Inside
  with Inspectors
{
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
    describe("Construction") {
      it("2-nd constructor test") {
        assert(Polynom.of((3,2)) == Polynom(Seq(Monom(3,2))))
      }
      it("2-nd constructor test: unsorted") {
        assert(Polynom.of((2,2), (3,2)) == Polynom(Seq(Monom(2,2), Monom(3,2))))
      }
      it("empty constructor") {
        assert(Polynom.empty == Polynom.of())
        assert(Polynom.empty == Polynom(Nil))
      }
    }

    describe("Representation") {
      it("should be represented in given order, w/o.sorting") {
        assert(Polynom.of((-12,3), (4,1), (-3,2), (-5,0)).toString == "-12x^3+4x-3x^2-5")
        assert(Polynom.of(( 12,3), (4,1), (-3,2), (-5,0)).toString ==  "12x^3+4x-3x^2-5")
        assert(Polynom.of((-12,3)).toString == "-12x^3")
        assert(Polynom.of(( 12,3)).toString ==  "12x^3")
      }
    }

    describe("Operations") {
      it("isZero:") {
        assert(Polynom(Nil).isZero)
        assert(Polynom.of().isZero)
        assert(Polynom.empty.isZero)
      }
      it("unary -") {
        assert(-Polynom.of() == Polynom.of())
        assert(-Polynom.of((-12,3), (4,1)) == Polynom.of((12,3), (-4,1)))
      }
      it("sort powers in descending order") {
        assert(Polynom.of().sorted == Polynom.of())
        assert(Polynom.of((2,2), (3,3)).sorted == Polynom.of((3,3), (2,2)))
      }
      it("squash: add the same powers 1") {
        assert(Polynom.of((2,3), (3,3)).squash == Polynom.of((5,3)))
      }
      it("squash: add the same powers 2") {
        assert(Polynom.of((1,0), (1,0), (1,0)).squash == Polynom.of((3,0)))
      }
      it("squash: remove K == 0") {
        assert(Polynom.of((2,3), (-2,3)).squash == Polynom.empty)
      }
      it("squash: sort after squash") {
        assert(Polynom.of((2,3), (3,3), (1,6)).squash == Polynom.of((1,6),(5,3)))
      }
      it("+:") {
        val p1 = Polynom.of((2,2), (3,3)) // 2x^2+3x^3
        val p2 = Polynom.of((3,2), (4,3)) // 3x^2+4x^3
        val p3 = p1 + p2                  // 7x^3+5x^2
        assert(p3 == Polynom.of((7,3),(5,2)))
      }
      it("*") {
        val p1 = Polynom.of((2,1), (3,2)) // 2x+3x^2
        val p2 = Polynom.of((3,3), (5,4)) // 3x^3+5x^4
        val p3 = p1 * p2                  // 15x^6+19x^5+6x^4
        assert(p3 == Polynom.of((15,6), (19,5), (6,4)))
      }
      it("/: just divide K by den") {
        assert(Polynom.of((4,1), (6,2)) / 2 == Polynom.of((3,2), (2,1)))
      }
      it("/: don't take into account fraction") {
        assert(Polynom.of((5,1), (7,2)) / 2 == Polynom.of((3,2), (2,1)))
      }
      it("/: remove K == 0 after 1") {
        assert(Polynom.of((1,3), (7,2)) / 2 == Polynom.of((3,2)))
      }
      it("/: remove K == 0 after 2") {
        assert(Polynom.of((1,3)) / 2 == Polynom.empty)
      }
    }
  }
}
