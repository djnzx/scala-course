package misc

import cats.implicits.catsSyntaxSemigroup
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MergeMaps extends AnyFunSuite with Matchers {

  val m1 = Map(1 -> Set("a", "b"))
  val m2 = Map(1 -> Set("b", "c"), 2 -> Set("d"))

  test("++ second overrides first") {
    m1 ++ m2 shouldBe Map(1 -> Set("b", "c"), 2 -> Set("d"))
  }

  test("|+| maps being combined") {
    m1 |+| m2 shouldBe Map(1 -> Set("a", "b", "c"), 2 -> Set("d"))
  }

}
