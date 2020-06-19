package fp_red.red09.algebra

import fp_red.red09.algebra.MonoPolyNom.{Monom, Polynom}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

// 28
class MonoPolyNomSpec extends AnyFunSpec with Matchers {
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

    describe("operations: is") {
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
    }
    
    describe("operations: -, *, /, to...") {
      it("unary -") {
        assert(- Monom(2, 3) == Monom(-2, 3))
        assert(- Monom(0, 3) == Monom( 0, 3))
      }
      it("*: multiply K and add P") {
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
        m.toPolynom shouldBe
          Polynom(Seq(m))
      }
    }
    
    describe("operations:+") {
      import MonoPolyNom.Monom.MonomOps
      
      it("+: squash the same power to one item") {
        Monom(2,3) + Monom(3,3) shouldBe
          Polynom.of((5, 3))
      }
      
      it("+: the same power and mirrored K should disappear") {
        Monom(-3,3) + Monom(3,3) shouldBe
          Polynom.of()
      }
      
      it("+: after adding sort in right order") {
        Monom(5,2) + Monom(3,3) shouldBe
          Polynom.of((3, 3), (5, 2))
      }
      
    }
  }

  describe("Polynom") {
    describe("Construction") {
      it("2-nd constructor test #1: Seq of Monoms") {
        assert(Polynom.of((3,2)) == Polynom(Seq(Monom(3,2))))
      }
      it("2-nd constructor test #2: one Monom") {
        assert(new Polynom(3,2) == Polynom(Seq(Monom(3,2))))
      }
      it("2-nd constructor test #3: Monoms unsorted") {
        assert(Polynom.of((2,2), (3,2)) == Polynom(Seq(Monom(2,2), Monom(3,2))))
      }
      it("2-nd constructor test #4: empty") {
        assert(Polynom.empty == Polynom.of())
        assert(Polynom.empty == Polynom(Nil))
      }
    }

    describe("Representation") {
      
      it("should be represented in given order, w/o.sorting 1") {
        Polynom.of((-12,3), (4,1), (-3,2), (-5,0)).toString shouldBe 
          "-12x^3+4x-3x^2-5"
      }
      it("should be represented in given order, w/o.sorting 2") {
        Polynom.of(( 12,3), (4,1), (-3,2), (-5,0)).toString shouldBe
          "12x^3+4x-3x^2-5"
      }
      it("should be represented in given order, w/o.sorting 3") {
        Polynom.of((-12,3)).toString shouldBe
          "-12x^3"
      }
      it("should be represented in given order, w/o.sorting 4") {
        Polynom.of(( 12,3)).toString shouldBe
          "12x^3"
      }
      
      it("representation for Hacker Rank") {
        assert(Polynom.of((-12,3), (4,1), (-3,2), (-5,0)).toStringHR == "-12x^3 + 4x - 3x^2 - 5")
        assert(Polynom.of(( 12,3), (4,1), (-3,2), (-5,0)).toStringHR ==  "12x^3 + 4x - 3x^2 - 5")
        assert(Polynom.of((-12,3)).toStringHR == "-12x^3")
        assert(Polynom.of(( 12,3)).toStringHR ==  "12x^3")
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
