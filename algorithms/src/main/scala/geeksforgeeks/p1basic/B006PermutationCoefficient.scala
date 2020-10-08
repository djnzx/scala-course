package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/permutation-coefficient/
  * https://en.wikipedia.org/wiki/Permutation
  * 
  * Permutation Coefficient
  * P(n, k) is used to represent 
  * the number of ways to obtain an ORDERED subset 
  * having K elements from a set of N elements.
  * 
  * 
  * P(n, k) = n * (n - 1) * ... * (n - k + 1)
  * P(n, k) = P(n-1, k) + k * P(n-1, k-1)
  * k-permutations of n
  * k <= n
  * 
  * P(n,0) = 1
  * P(n,1) = n
  *
  * C(n, k) solved [[geeksforgeeks.p1basic.B005BinomialCoefficient]]
  */
object B006PermutationCoefficient {
  
  def pNk_it(n: Int, k: Int) = 
    (n-k+1 to n).product
  
  def pNk(n: Int, k: Int): Int = {
    require(n > 0 && k >= 0)
    if (k > n) return 0
    if (k == 0) return 1
    if (k == 1) return n
    
    val a = Array.ofDim[Int](n+1, n+1)
    (1 to n).foreach { n => 
      (0 to n).foreach { k => 
        a(n)(k) = k match {
          case 0 => 1
          case 1 => n
          case _ => a(n - 1)(k) + k * a(n - 1)(k - 1)   
        }
      }
    }
//    printMatrix(a)
    a(n)(k)
  }
}

class P006PermutationCoefficientSpec extends ASpec {
  import B006PermutationCoefficient._
  
  it("1") {
    val data = Seq(
      (1, 0) -> 1,
      (1, 1) -> 1,
      
      (2, 0) -> 1,
      (2, 1) -> 2,
      (2, 2) -> 2,

      (3, 0) -> 1,
      (3, 1) -> 3,
      (3, 2) -> 6,
      (3, 3) -> 6,

      (4, 0) -> 1,
      (4, 1) -> 4,
      (4, 2) -> 12,
      (4, 3) -> 24,
      (4, 4) -> 24,
      
      (100, 0) -> 1,   // any N, => 1
      (200, 1) -> 200, // k = 1, => N 
      (3, 5) -> 0      // k > n  => 0 
    )
    val impls = Seq(
      (pNk _).tupled,
      (pNk_it _).tupled
    )
    
    runAllSD(data, impls)
  }
  
}