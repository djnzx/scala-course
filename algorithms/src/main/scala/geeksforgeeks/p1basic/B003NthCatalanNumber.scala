package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/program-nth-catalan-number/
  * c0 = 1
  * c1 = c0*c0 = 1
  * c2 = c0c1 + c1c0 = 2
  * c3 = c0c2 + c1c1 + c2c0 = 5
  */
object B003NthCatalanNumber {
  def nthCatalanNumber(n: Int): Int =
    if (n == 0) 1
    else {
      val a = Array.ofDim[Int](n + 1)
      a(0) = 1
      a(1) = 1

      def nth(x: Int): Int = {
        val x2 = x / 2
        val part = 2 * (0 until x2).foldLeft(0) { (acc, i) => acc + a(i) * a(x - 1 - i) }

        x % 2 match {
          case 0 => part
          case 1 => part + a(x2) * a(x2)
        }
      }

      (2 to n).foreach { x => a(x) = nth(x) }
      a(n)
    }
}

class B003NthCatalanNumberSpec extends ASpec {
  import B003NthCatalanNumber._

  it("1") {
    val data = Seq(
      0 -> 1,
      1 -> 1,
      2 -> 2,
      3 -> 5,
      4 -> 14,
      5 -> 42,
      6 -> 132,
      7 -> 429,
      8 -> 1430,
      9 -> 4862,
    )

    runAllD(data, nthCatalanNumber _)
  }

}