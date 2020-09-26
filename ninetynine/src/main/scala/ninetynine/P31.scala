package ninetynine

/**
  * Determine whether a given integer number is prime
  * 
  * http://primes.utm.edu/prove/index.html
  * http://article.gmane.org/gmane.comp.lang.haskell.cafe/19470
  */
object P31 {

  // LAZY PRIMES GENERATION
  def isPrime(x: Int): Boolean = (x > 1) && (primes takeWhile { _ <= math.sqrt(x) } forall { x % _ != 0 })
  val primes = LazyList.cons(2, LazyList.from(3, 2) filter isPrime)
  
  def isPrimeNaive(a: Int): Boolean = 
    (a > 1) && Stream.from(2).take(math.sqrt(a).toInt+1).filter(_ < a).forall(a % _ != 0)
}

class P31Spec extends NNSpec {
  import P31._
  
  it("1") {
    val t = Seq(2,3,5,7,11,13,17,19,23,29,31).map(_->true)
    val f = Seq(1,4,6,8,9,10,12,14,15,16,18,20).map(_->false)

    (t ++ f).toMap
      .foreach { case (in, out) => isPrime(in) shouldEqual out }
  }
  
  it("2") {
    println(primes.take(30))
  }
  
}
