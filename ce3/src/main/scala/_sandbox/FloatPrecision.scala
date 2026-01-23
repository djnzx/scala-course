package _sandbox

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FloatPrecision extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("leet0001") {
    val target: Int = ???
    val nums: Array[Int] = ???
    LazyList.range(1, nums.length)
      .flatMap { i =>
        LazyList.range(1, nums.length)
          .filter(j => i != j)
          .map(j => (i, j))
      }
      .find { case (i, j) => nums(i) + nums(j) == target }
      .map { case (i, j) => Array(i, j) }
      .getOrElse(???)
  }

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

  // https://www.h-schmidt.net/FloatConverter/IEEE754.html
  // https://en.wikipedia.org/wiki/IEEE_754
  test("java-ish 1") {
    import java.lang.Double
    import java.lang.Long

    val x = 0.1 + 0.2     // 0.3 = 3 / 10
    System.out.println(x) // 0.30000000000000004

    var bits = Double.doubleToLongBits(x)
    var binary = Long.toBinaryString(bits)
    System.out.println(binary)
    // 11111111010011001100110011001100110011001100110011001100110100

    System.out.println(binary.length)

    val y = 0.25 + 0.5 // 1/2 + 1/4 = 1*2^-2 + 1*2^-1
    System.out.println(y)
    // 11111111101000000000000000000000000000000000000000000000000000

    bits = Double.doubleToLongBits(y)
    binary = Long.toBinaryString(bits)
    System.out.println(binary)
    System.out.println(binary.length)

  }

  test("java-ish 2") {
    val x: Float = 16_777_216
    val y: Float = 16_777_216 + 1
    System.out.println(x == y)

    System.out.printf("%8f\n", x)
    System.out.printf("%8f\n", x + 1)
    System.out.printf("%8f\n", x + 2)
  }

}
