package fp_red.red09.p2concrete.monom

import fp_red.red09.p1impl.Reference
import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AnyFunSpec

// 21
class MonomParserSpec extends AnyFunSpec with Matchers {

  describe("Monom") {
    val R = Reference
    import R._
    
    import MonomParser.{R => _, _}

    describe("experiments: x^p") {
      val xp = char('x') ** (char('^') *> digitsSigned).many.map {
        case Nil => 1
        case h :: Nil => h.toInt
      }
      it("x^p: x") {
        R.run(xp)("x") shouldBe Right(('x', 1))
      }
      it("x^p: x^0") {
        R.run(xp)("x^0") shouldBe Right(('x', 0))
      }
      it("x^p: x^1") {
        R.run(xp)("x^1") shouldBe Right(('x', 1))
      }
      it("x^p: x^-1") {
        R.run(xp)("x^-1") shouldBe Right(('x', -1))
      }
      it("x^p: x^2") {
        R.run(xp)("x^2") shouldBe Right(('x', 2))
      }
      it("x^p: x^20") {
        R.run(xp)("x^20") shouldBe Right(('x', 20))
      }
      it("x^p: x^-23") {
        R.run(xp)("x^-23") shouldBe Right(('x', -23))
      }
    }

    describe("experiments: n*x") {
      val oi = opt(integer)
      it("n*x:1") {
        R.run(oi)("x") shouldBe Right(None)
      }
      it("n*x:2") {
        R.run(oi)("-x") shouldBe Right(None)
      }
      it("n*x:3") {
        R.run(oi)("2x") shouldBe Right(Some(2))
      }
      it("n*x:4") {
        R.run(oi)("-3x") shouldBe Right(Some(-3))
      }
    }

    describe("experiments: nx^p") {
      describe("1 by 1 w.o combinations") {
        it("nx^p: 3x^5") {
          R.run(nxp)("3x^5") shouldBe Right(NP(3, 5))
        }
        it("nx^p: 4x") {
          R.run(nx1)("4x") shouldBe Right(NP(4, 1))
        }
        it("nx^p: 6") {
          R.run(n_)("6") shouldBe Right(NP(6, 0))
        }
        it("nx^p: x^2") {
          R.run(xp)("x^2") shouldBe Right(NP(1, 2))
        }
        it("nx^p: x") {
          R.run(x_)("x") shouldBe Right(NP(1, 1))
        }
      }
      describe("combinations...") {
        it("nx^p: 3x^5") {
          R.run(monom)("3x^5") shouldBe Right(NP(3, 5))
        }
        it("nx^p: 4x") {
          R.run(monom)("4x") shouldBe Right(NP(4, 1))
        }
        it("nx^p: 6") {
          R.run(monom)("6") shouldBe Right(NP(6, 0))
        }
        it("nx^p: x^2") {
          R.run(monom)("x^2") shouldBe Right(NP(1, 2))
        }
        it("nx^p: x") {
          R.run(monom)("x") shouldBe Right(NP(1, 1))
        }
      }
    }

  }

}
