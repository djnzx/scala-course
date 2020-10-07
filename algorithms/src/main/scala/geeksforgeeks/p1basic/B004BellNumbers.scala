package geeksforgeeks.p1basic

import tools.fmt.Fmt.{prettyMatrix, printMatrix}
import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/bell-numbers-number-of-ways-to-partition-a-set/
  * https://www.geeksforgeeks.org/wp-content/ql-cache/quicklatex.com-6ea5decd6aa25a444e0ed57bf5a1e856_l3.svg
  * Number of ways to Partition a Set
  * 
  * Sum k=0..N (S(n,k))
  * S(n+1,k) = k*S(n, k) +S(n, k-1)
  */
object B004BellNumbers {

  def bell(n: Int): Int = {
    if (n < 2) return 1
    val a = Array.ofDim[Int](n, n)
    a(0)(0) = 1
    (1 until n).foreach { n => 
      (0 to n).foreach { k =>
        a(n)(k) = 
          if (k==0) a(n-1)(n-1)
          else      a(n-1)(k-1) + a(n)(k-1)
//        println(s"n=$n, k=$k")
//        printMatrix(a)
      }
    }
    a(n-1)(n-1)
  }
}

class B004BellNumbersSpec extends ASpec {
  import B004BellNumbers._
  it("1") {
    val data = Seq(
      0 -> 1,
      1 -> 1,
      2 -> 2,
      3 -> 5,
      4 -> 15,
      5 -> 52,
      6 -> 203,
      7 -> 877,
      8 -> 4140,
    )
    runAllD(data, bell _)
  }
}
