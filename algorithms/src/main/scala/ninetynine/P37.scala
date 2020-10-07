package ninetynine

import tools.spec.ASpec

/**
  * Calculate Euler's totient function phi(m)
  * Smart implementation
  * for brute force one -> [[P34]]
  */
object P37 {
  import P36._
  
  /** a^b^ */
  def ab(a: Int, b: Int) = (1 to b).foldLeft(1L) { (acc, _) => acc * a }
  
  /** (m-1)*m^(p-1)^ */
  def mp(m: Int, p: Int) = (m - 1).toLong * ab(m, p - 1)
  
  /** phi(m) = (p1-1)*p1^(m1-1)^^ * (p2-1)*p2^(m2-1)^^ * (p3-1)*p3^(m3-1)^ * ...  */
  def phi(n: Int) =
    primeFactorMultiplicity(n).foldLeft(1L) { case (acc, (m, p)) => acc * mp(m, p) } 
}

class P37Spec extends ASpec {
  import P36._
  import P37._
  
  it("prime factors") {
    primeFactorMultiplicity(10) shouldEqual Seq(2->1, 5->1)
  }
  
  it("a^b") {
    Seq(
      17 -> 1 -> 17,
      111 -> 0 -> 1,
      2 -> 3 -> 8,
      3 -> 3 -> 27,
    )
      .foreach { case ((a, b), r) => 
        ab(a, b) shouldEqual r
      }
  }
  
  it("3") {
    Seq(
      2 -> 1 -> 1,
      5 -> 1 -> 4,
      7 -> 3 -> 294,
    )
      .foreach { case ((a, b), r) =>
        mp(a, b) shouldEqual r
      }
  }
  
  it("5") {
    phi(10) shouldEqual 4
  }
  
}

