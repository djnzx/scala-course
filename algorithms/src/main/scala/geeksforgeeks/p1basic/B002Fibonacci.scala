package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/program-for-nth-fibonacci-number/
  */
object B002Fibonacci {

  def fibo(n1: Int, n2: Int, c: Int): Int = c match {
    case 0 => 0
    case 1 => n2
    case _ => fibo(n2, n1+n2, c-1)
  }
  def fibo(c: Int): Int = fibo(0,1, c)

}

class B002FibonacciSpec extends ASpec {
  import B002Fibonacci._
  
  it("1") {
    val data = Seq(
      0 -> 0,
      1 -> 1,
      2 -> 1,
      3 -> 2,
      4 -> 3,
      5 -> 5,
      6 -> 8,
      9 -> 34
    )
    runAll(data, fibo)
  }
}
