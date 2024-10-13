package az.be

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object StringRotation {

  def rotate(s: String, n0: Int): String =
    n0 % s.length match {
      // right
      case x if x > 0 =>
        val r = s.substring(s.length - x)
        val l = s.substring(0, s.length - x)
        r + l
      // left
      case x if x < 0 =>
        val l = s.substring(0, -x)
        val r = s.substring(-x)
        r + l
      case _          => s
    }

}

class StringRotation extends AnyFunSuite with ScalaCheckPropertyChecks {
  import StringRotation._

  test("1") {
    val testCases = Table(
      "",
      0,
      8,
      -8,
      1,
      2,
      10,
      -1,
      -2,
      -10
    )
    val s = "abcdefgh"
    forAll(testCases) { shift =>
      val s2 = rotate(s, shift)
      pprint.log(s2 -> shift)
    }
  }
}
