package myhaslon

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MuhaSlon extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  def mkN(xs: Int*): Int = {
    def go(xs: List[Int], m: Int): Int = xs match {
      case Nil     => 0
      case x :: xs => x * m + go(xs, m * 10)
    }
    go(xs.reverse.toList, 1)
  }

  def isValid(xs: Int*): Boolean = xs.toList match {
    case List(m, y, x, a, s, l, o, n) => mkN(m, y, x, a) * a == mkN(s, l, o, n)
    case _                            => false
  }

  def show(xs: Int*): String = xs.toList match {
    case List(m, y, x, a, s, l, o, n) =>
      val q = mkN(m, y, x, a)
      val w = mkN(s, l, o, n)
      s"$q * $a = $w"
    case _                            => "invalid input"
  }

  def comb(xs: List[Int]): List[List[Int]] = xs match {
    case Nil => List(Nil)
    case xs  => xs.flatMap(x => comb(xs.filter(_ != x)).map(x :: _))
  }

  def solve: List[List[Int]] = comb(1 to 8 toList).filter(isValid)

  test("mkN") {
    mkN() shouldBe 0
    mkN(1, 2, 3) shouldBe 123
    mkN(1, 2, 3, 4) shouldBe 1234
  }

  test("show") {
    show(1, 2, 3, 4, 5, 6, 7, 8) shouldBe "1234 * 4 = 5678"
  }

  test("isValid") {
    isValid(1, 2, 3, 4, 5, 6, 7, 8) shouldBe false
    isValid(1, 7, 8, 2, 3, 5, 6, 4) shouldBe true
  }

  test("solution with output") {
    solve
      .map(show)
      .foreach(println)
  }

}
