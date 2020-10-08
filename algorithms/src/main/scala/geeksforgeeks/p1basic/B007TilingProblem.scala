package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/tiling-problem/
  * how many ways to cover 2xN area with 2x1 plates
  * 
  * tiling(1) = 1
  * tiling(2) = 2
  * tiling(n) = tiling(n-1) + tiling(n-2) 
  * 
  * actually it's a modified Fibonacci, but here is 
  * the canonical dynamic solution
  */
object B007TilingProblem {
  def tiling(n: Int): Int = {
    require(n > 0)
    if (n <= 2) return n
    
    val a = Array.ofDim[Int](n+1)
    a(1) = 1
    a(2) = 2
    (3 to n).foreach { i => a(i) = a(i-1) + a(i-2) }
    a(n)
  }
}

class B007TilingProblemSpec extends ASpec {
  import B007TilingProblem._
  
  it("1") {
    val data = Seq(
      1 -> 1,
      2 -> 2,
      3 -> 3,
      4 -> 5,
      5 -> 8,
      6 -> 13,
      7 -> 21,
      8 -> 34,
    )
    
    runAllD(data, tiling)
  }
  
}
