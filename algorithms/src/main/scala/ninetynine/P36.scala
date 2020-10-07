package ninetynine

import tools.spec.ASpec

/**
  * Determine the prime factors of a given positive integer
  */
object P36 {
  import P35._
  import P10._
  
  def primeFactorMultiplicity(a: Int) =
    pack(primeFactors(a))

  def primeFactorMultiplicityToMap(a: Int) =
    primeFactorMultiplicity(a).toMap
}

class P36Spec extends ASpec {
  import P36._
  
  it("1") {
    Seq(
      8 -> Seq((2,3)),
      24 -> Seq((2,3), (3,1)),
      315 -> Seq((3, 2), (5, 1), (7, 1)),
    )
    .foreach { case (in, out) => 
      primeFactorMultiplicity(in) shouldEqual out
      primeFactorMultiplicityToMap(in) shouldEqual out.toMap
    }
  }
}
