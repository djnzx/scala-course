package _sandbox

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FloatPrecision extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("1") {
    val x: Float = 256 * 256 * 256
    x shouldBe x + 1
  }

  test("2") {
    val x: Float = 256 * 256 * 256 * 2
    x shouldBe x + 2
  }

  test("3") {
    val x: Float = 256 * 256 * 256 * 4
    x shouldBe x + 4
  }

  test("4") {
    val x: Float = 256 * 256 * 256 * 16
    x shouldBe x + 16
  }

  test("5") {
    val x: Float = 256 * 256 * 256 * 64
    x shouldBe x + 64
  }

  test("6") {
    val x: Float = Float.NaN
    x != x shouldBe true
  }

  test("representation") {
    val x: Float = Float.NaN
    val s = java.lang.Float.floatToIntBits(x).toBinaryString
    val s2 = "0".repeat(32 - s.length) + s
    val s3 = s"${s2(0)} ${s2.substring(1, 9)} ${s2.substring(9)}"
    pprint.log(s2)
    pprint.log(s3)
  }

}
