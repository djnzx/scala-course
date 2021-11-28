package hackerrankfp.d221126

import hackerrankfp.d221126.Euclidean.gcdExtended
import hackerrankfp.d221126.Euclidean.modDivide
import hackerrankfp.d221126.Euclidean.modInverse
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

object Euclidean {

  implicit class EuclideanModuloSyntax(private val a: Int) {
    def /%(b: Int): (Int, Int) = {
      val q = a / b
      val r = a - q * b

      /** euclidean modulo must be positive */
      if (r >= 0) (q, r)
      else { // r < 0
        if (a < 0 && b > 0) {
          val r1 = r + b
          val q1 = (a - r1) / b
          (q1, r1)
        } else {
          val q1 = q + math.signum(q)
          val r1 = a - q1 * b
          (q1, r1)
        }
      }
    }
    def %%(b: Int) = /%(b)._2
  }

  def gcd(a: Int, b: Int): Int =
    if (b == 0) a
    else gcd(b, a % b)

  def modInverse(b: Int, m: Int): Int = {
    val (gcd, x, _) = gcdExtended(b, m)
    if (gcd != 1) -1 // if b and m are not co-prime
    else (x % m + m) % m // m is added to handle negative x
  }

  def gcdExtended(a: Int, b: Int): (Int, Int, Int) =
    if (a == 0) (b, 0, 1) // Base Case
    else {
      val (gcd, x1, y1) = gcdExtended(b % a, a);

      val x2 = y1 - (b / a) * x1
      val y2 = x1

      (gcd, x2, y2)
    }

  def modDivide(a: Int, b: Int, m: Int): Int = {
    val inv: Int = modInverse(b, m)
    if (inv == -1) -1
    else (inv * a % m) % m
  }

}

class EuclideanSpec extends AnyFunSpec with Matchers {

  import Euclidean.EuclideanModuloSyntax

  describe("modulo") {

    it("7 /% 3") {
      7 /% 3 shouldEqual (2, 1)
    }

    it("7 /% -3") {
      7 /% -3 shouldEqual (-2, 1)
    }

    it("-7 /% 3") {
      -7 /% 3 shouldEqual (-3, 2)
    }

    it("-7 /% -3") {
      -7 /% -3 shouldEqual (3, 2)
    }

    it("-7 %% -3") {
      -7 %% -3 shouldEqual 2
    }

    it("-3 %% 4") {
      -3 /% 4 shouldEqual (-1, 1)
    }

    it("-1402 %% 1000000007") {
      -1402 %% 1000000007 shouldEqual 999998605
    }

    it("non euclidean") {
      println(-3 / 4) // 0 ...-1
      println(-3 % 4) // -3....1
    }
  }

  describe("gcd") {
    import Euclidean.gcd

    it("1a") {
      gcd(5, 7) shouldEqual 1
    }

    it("1b") {
      gcd(6, 12) shouldEqual 6
    }

    it("1c") {
      gcd(12, 6) shouldEqual 6
    }

    it("1d") {
      gcd(24, 36) shouldEqual 12
    }

    it("1e") {
      gcd(1053, 325) shouldEqual 13
    }
  }

  describe("gcd extended") {
    it("1") {
      println(gcdExtended(24, 36)) // (12,-1,1)
    }

    it("modinv") {
      val r = modInverse(10, 1000000005)
      println(r)
    }

    it("mod div") {
      val r = modDivide(1, 10, 1000000007)
      println(r)
    }

  }

}
