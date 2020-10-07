package ninetynine

import tools.spec.ASpec

/**
  * Determine the greatest common divisor of two positive integer numbers
  */
object P32 {

  def gcd(a: Int, b: Int): Int = a % b match {
    case 0 => b
    case r => gcd(b, r)
  }

}

class P32Spec extends ASpec {
  import P32._
  
  it("1") {
    Seq(
      (1,2) -> 1,
      (10,15) -> 5,
      (15,5) -> 5,
      (36,24) -> 12,
      (63,36) -> 9,
    )
      .foreach { case ((a, b), r) => gcd(a, b) shouldEqual r }
  }
}
