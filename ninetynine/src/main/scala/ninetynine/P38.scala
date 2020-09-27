package ninetynine

import ninetynine.P31.primes

/**
  * complexity investigation
  */
class P38 extends NNSpec {
  import P34._
  import P37._
  
  def time[A](body: => A) = {
    val startedAt = System.currentTimeMillis()
    val x = body
    val delta = System.currentTimeMillis() - startedAt
    (x, delta)
  }
  
  def pretty[A](data: (A, Long), msg: String) =
    println(s"$msg, res:${data._1}, time:${data._2}ms")
  
  it("1") {
    val N = 10090000
    // load
    pretty(time(primes.takeWhile(_ <= math.sqrt(N.toDouble)).force), "warmup")
    pretty(time(totient(N)), "1-st ver.")
    pretty(time(phi(N)), "2-nd ver.")
  }

}
