package xonai

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Main._
  import Pretty.show

  def mkStringColumn(xs: String*) = {
    val sc = new StringColumn
    sc.length = xs.map(_.length).toArray
    sc.offset = Iterator.unfold(0 -> 0) {
      case (0, _)                               => Some(0 -> (1, 0))
      case (idx, off) if idx < sc.length.length =>
        val off2 = off + sc.length(idx - 1)
        Some(off2 -> (idx + 1, off2))
      case _                                    => None
    }.toArray
    sc.buffer = xs.flatMap(_.getBytes).toArray

    sc
  }

  test("mkStringColumn") {
    //              offset   0        5     7    8    9
    //              length   5        2     1    1    7
    val sc = mkStringColumn("Hello", "HI", "A", "B", "hiThere")
    sc.length shouldBe Array(5, 2, 1, 1, 7)
    sc.offset shouldBe Array(0, 5, 7, 8, 9)
    sc.buffer shouldBe "HelloHIABhiThere".getBytes
    show(sc)
  }

  test("=A something is null") {
    val sc = new StringColumn

    isEqualToA(1, sc) shouldBe false
  }

  test("=A diff len") {
    val sc = new StringColumn
    sc.offset = Array(0, 5, 7, 8, 9)
    sc.length = Array(5, 2, 1, 1)
    sc.buffer = Array("Hello".getBytes, "HI".getBytes, "A".getBytes, "B".getBytes, "hiThere".getBytes).flatten

    isEqualToA(1, sc) shouldBe false
  }

  test("=A") {
    val sc = new StringColumn
    sc.offset = Array(0, 5, 7, 8, 9)
    sc.length = Array(5, 2, 1, 1, 3)
    //                 0                 1              2             3             4
    sc.buffer = Array("Hello".getBytes, "HI".getBytes, "A".getBytes, "B".getBytes, "hiThere".getBytes).flatten

    isEqualToA(33, sc) shouldBe false // not exist in array
    isEqualToA(0, sc) shouldBe false  // longer
    isEqualToA(3, sc) shouldBe false  // not equal
    isEqualToA(2, sc) shouldBe true   // equal
  }

  test("isLikePromoSummer") {
    val t = true
    val f = false
    val testCases = Seq(
      ""               -> f,
      "a"              -> f,
      "whatever"       -> f,
      "PROM0SUMMER"    -> f,
      "1PROMOSUMMER"   -> f,
      "PROMOSUMMER2"   -> f,
      "PR_MOSUMMER"    -> f,
      "PROMOSUM_ER"    -> f,
      "PROMOSUMMER"    -> t,
      "PROMO SUMMER"   -> t,
      "PROMO123SUMMER" -> t
    )
    val sc = mkStringColumn(testCases.map(_._1): _*)

    show(sc)

    testCases.zipWithIndex.foreach { case ((_, expected), idx) =>
      isLikePromoSummer(idx, sc) shouldBe expected
    }
  }

  test("copyOfIndexes") {
    val sc = mkStringColumn("ab", "CDE", "f", "GHIJ", "q", "lkmk", "Z")
    val sc2 = copyAtIndexes(sc, Array(1,3,6),3)
    show(sc)
    println()
    show(sc2)

  }

}
