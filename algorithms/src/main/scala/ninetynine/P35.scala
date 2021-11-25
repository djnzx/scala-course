package ninetynine

import tools.spec.ASpec

/** Determine the prime factors of a given positive integer it tries to divide by all prime numbers
  */
object P35 {
  import scala.collection.immutable.LazyList.cons
  import P31.isPrime
  import P31.primes

  private def primeFactors(n: Int, primes: LazyList[Int], factors: List[Int]): List[Int] = (n, primes) match {
    case (1, _) => factors sorted
    case (_, cons(ph, _)) if n % ph == 0 => primeFactors(n / ph, primes, ph :: factors)
    case (_, cons(_, pt)) => primeFactors(n, pt, factors)
    case _                => ???
  }

  def primeFactors(n: Int): List[Int] = isPrime(n) match {
    case true => List(n)
    case _    => primeFactors(n, primes, Nil)
  }

  def primeFactorsDistinct(n: Int) = primeFactors(n).toSet

}

class P35Spec extends ASpec {
  import P35._

  it("1") {
    val data = Seq(
      315 -> List(3, 3, 5, 7),
    )
    runAll(data, primeFactors)
  }
}
