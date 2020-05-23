package topics.recursion.fibo

import scala.annotation.tailrec

object FiboApp3 extends App {

  def fib(n: Int): Int = {
    @tailrec
    def fibTail(n: Int, curr: Int, prev: Int): Int = n match {
      case 1 => curr
      case _ => fibTail(n-1, curr + prev, curr)
    }
    fibTail(n, 1, 0)
  }

  println(fib(10))
}
