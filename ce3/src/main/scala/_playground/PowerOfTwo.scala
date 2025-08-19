package _playground

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.annotation.tailrec

class PowerOfTwo extends AnyFunSuite with Matchers {

  def isPowerOfTwo(x: Int): Boolean = {

    @tailrec
    def go(x: Int, count: Int): Boolean =
      if (count > 1) false
      else if (x == 0) count < 2
      else go(x >> 1, count + (x & 1))

    go(x, 0)
  }

  test("true") {
    Seq(0, 1, 2, 4, 8, 16, 32, 64, 128, 512, 1024)
      .foreach(x => isPowerOfTwo(x) shouldBe true)
  }

  test("false") {
    Seq(3, 5, 7, 9, 31, 1023)
      .foreach(x => isPowerOfTwo(x) shouldBe false)
  }

}
