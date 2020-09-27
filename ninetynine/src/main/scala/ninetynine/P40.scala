package ninetynine


/**
  * Goldbach's conjecture says that 
  * every positive even number greater than 2 is the sum of two prime numbers. 
  * E.g. 28 = 5 + 23.
  * 
  * It is one of the most famous facts in number theory that
  * has not been proved to be correct in the general case.
  * It has been numerically confirmed up to very large numbers
  */
object P40 {
  import P31.{isPrime, primes}

  def goldbach(n: Int): Option[(Int, Int)] =
    if (n % 2 != 0 || n <= 0) None
    else
      primes.takeWhile(_ < n).find(x => isPrime(n - x)) match {
        case Some(x) => Some((x, n-x))
        case _       => None
      }
}

class P40Spec extends NNSpec {
  import P40._
  
  it("1") {
    Seq(
      28 -> (5, 23),
    )
      .foreach { case (n, r) =>
        goldbach(n) shouldEqual Some(r)
      }
  }
}
