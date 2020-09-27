package ninetynine

/**
  * Calculate Euler's totient function phi(m).
  */
object P34 {
  import P33.areCoPrime
  
  def totient(m: Int): Int = (1 to m).count(areCoPrime(_, m)) 
  
}

class P34Spec extends NNSpec {
  import P34._
  
  it("1") {
    totient(10) shouldEqual 4
  }
  
}
