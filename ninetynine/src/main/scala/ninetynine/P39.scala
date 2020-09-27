package ninetynine

/**
  * A list of prime numbers
  */
object P39 {
  import P31.primes
  
  def primesRange(r: Range): List[Int] = primesRange(r.min, r.max)
    
  def primesRange(mn: Int, mx: Int): List[Int] =
    primes dropWhile(_ < mn) takeWhile(_ <= mx) toList

}

class P39Spec extends NNSpec {
  import P39._
  
  it("") {
    Seq(
      7 -> 31 -> Seq(7, 11, 13, 17, 19, 23, 29, 31)
    )
      .foreach { case ((mn, mx), r) => 
        primesRange(mn, mx) shouldEqual r
      }
  }
}