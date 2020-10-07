package ninetynine

import tools.spec.ASpec

/**
  * Determine whether two positive integer numbers are coprime
  */
object P33 {
  import P32._
  
  def areCoPrime(a: Int, b: Int) = gcd(a, b) == 1

}

class P33Spec extends ASpec {
  import P33._
  
  it("1") {
    
    val f = Seq(
      (10,15),
      (15,5), 
      (36,24),
      (63,36),
    ).map(_->false)
    
    val t = Set(
      (1,93),
      (17,39),
      (35,64),
      (101,71),
    ).map(_->true)
      
    (t ++ f).toMap
      .foreach { case ((a, b), r) => areCoPrime(a, b) shouldEqual r }
  }
}
