package ninetynine

import tools.spec.ASpec
import tools.Timed.timed

/**
  * complexity investigation
  */
class P38 extends ASpec {
  import P31.primes
  import P34._
  import P37._
  
  def pretty[A](data: (A, Long), msg: String) =
    println(s"$msg, res:${data._1}, time:${data._2}ms")
  
  it("1") {
    val N = 10090000
    // load
    pretty(timed(primes.takeWhile(_ <= math.sqrt(N.toDouble)).force), "warmup")
    pretty(timed(totient(N)), "1-st ver.")
    pretty(timed(phi(N)), "2-nd ver.")
  }

}
