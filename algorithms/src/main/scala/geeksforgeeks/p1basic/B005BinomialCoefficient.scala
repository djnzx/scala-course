package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/binomial-coefficient-dp-9/
  * https://en.wikipedia.org/wiki/Binomial_coefficient
  * https://en.wikipedia.org/wiki/Pascal%27s_triangle
  * n choose k
  * another solution in [[glovo.TaskA.solution]]
  * 
   *          P(n, k)
  * C(n,k) = ---------
  *           P(k, k)
  * 
  * P(n, k) solved [[geeksforgeeks.p1basic.B006PermutationCoefficient]]
  */
object B005BinomialCoefficient {
  def nCk(n: Int, k: Int): Int = {
    require(n > 0 && k >=0 && k <= n)
    val a = Array.ofDim[Int](n+1,n+1)
    (0 to n).foreach { n =>
      a(n)(0) = 1
      a(n)(n) = 1
      (1 until n).foreach { k =>
        a(n)(k) = a(n-1)(k-1) + a(n-1)(k)
      }
    }
    a(n)(k)
  }
}

object B005BinomialCoefficientData {
  val data = Seq(
    // n  k
    (1, 0) -> 1,
    (1, 1) -> 1,

    (2, 0) -> 1,
    (2, 1) -> 2,
    (2, 2) -> 1,

    (3, 0) -> 1,
    (3, 1) -> 3,
    (3, 2) -> 3,
    (3, 3) -> 1,

    (4, 0) -> 1,
    (4, 1) -> 4,
    (4, 2) -> 6,
    (4, 3) -> 4,
    (4, 4) -> 1,

    (5, 0) -> 1,
    (5, 1) -> 5,
    (5, 2) -> 10,
    (5, 3) -> 10,
    (5, 4) -> 5,
    (5, 5) -> 1
  )

}

class B005BinomialCoefficientSpec extends ASpec {
  import B005BinomialCoefficient._
  import B005BinomialCoefficientData._
  
  it("1") {
    runAllD(data, (nCk _).tupled)
  }
    
}