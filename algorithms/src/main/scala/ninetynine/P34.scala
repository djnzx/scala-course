package ninetynine

import tools.spec.ASpec

/**
  * Calculate Euler's totient function phi(m).
  * Brute Force implementation
  * for smart one -> [[P37]]
  */
object P34 {
  import P33.areCoPrime
  
  def totient(m: Int): Int = (1 to m).count(areCoPrime(_, m)) 
  
}

class P34Spec extends ASpec {
  import P34._
  
  it("1") {
    totient(10) shouldEqual 4
  }
  
}
