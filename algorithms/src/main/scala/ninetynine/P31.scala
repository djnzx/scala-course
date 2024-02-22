package ninetynine

/** Determine whether a given integer number is prime
  *
  * [[http://primes.utm.edu/prove/index.html]]
  * [[http://article.gmane.org/gmane.comp.lang.haskell.cafe/19470]]
  * [[https://aperiodic.net/phil/scala/s-99/#p31]]
  */
object P31 {

  // LAZY PRIMES GENERATION
  def isPrime(x: Int): Boolean = (x > 1) && primes.takeWhile(_ <= math.sqrt(x)).forall(x % _ != 0)
  val primes: LazyList[Int] = 2 #:: LazyList.from(3, 2).filter(isPrime)

  def isPrimeNaive(a: Int): Boolean =
    (a > 1) && LazyList.from(2).take(math.sqrt(a).toInt + 1).forall(a % _ != 0)
}
