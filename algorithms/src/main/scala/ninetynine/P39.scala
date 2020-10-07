package ninetynine

import tools.spec.ASpec

/**
  * A list of prime numbers
  */
object P39 {
  import P31.primes

  def primesRange(r: Range): List[Int] = primesRangeMM(r.min, r.max)

  def primesRangeMM(mn: Int, mx: Int): List[Int] =
    primes dropWhile(_ < mn) takeWhile(_ <= mx) toList

}

class P39Spec extends ASpec {
  import P39._

  it("") {
    val data = Seq(
      7 -> 31 -> Seq(7, 11, 13, 17, 19, 23, 29, 31)
    )

    runAll(data, (primesRangeMM _).tupled)
  }
}