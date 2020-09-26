package ninetynine

/**
  * Determine the prime factors of a given positive integer
  */
object P35 {
  import scala.collection.immutable.LazyList.cons
  import P31.{isPrime, primes}

  private def primeFactors(n: Int, primes: LazyList[Int], factors: List[Int]): List[Int] = (n, primes) match {
    case (1, _)                          => factors sorted
    case (_, cons(ph, _)) if n % ph == 0 => primeFactors(n / ph, primes, ph :: factors)
    case (_, cons(_, pt))                => primeFactors(n,      pt,           factors)
  }

  def primeFactors(n: Int): List[Int] = 
    if (isPrime(n)) List(n)
    else primeFactors(n, primes, Nil)

}

class P35Spec extends NNSpec {
  import P35._
  
  it("1") {
    primeFactors(315) shouldEqual List(3,3,5,7)
  }
}
