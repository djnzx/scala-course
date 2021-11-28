package hackerrankfp.d221126

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object EuclideanModulo {

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

}

class EuclideanModuloSpec extends AnyFunSpec with Matchers {

  import EuclideanModulo.EuclideanModuloSyntax

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
