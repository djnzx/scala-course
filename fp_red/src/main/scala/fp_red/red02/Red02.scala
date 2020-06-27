package fp_red.red02

import scala.annotation.tailrec

object Chapter2 {

  def abs(n: Int): Int =
    if (n < 0) -n
    else n

  private def formatAbs(x: Int) = {
    val msg = "The absolute value of %d is %d"
    msg.format(x, abs(x))
  }

  def formatAbs2(x: Int) =
    f"The absolute value of $x is ${abs(x)}"

  // A definition of factorial, using a local, tail recursive function
  def factorial(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int =
      if (n <= 0) acc
      else go(n-1, n*acc)

    go(n, 1)
  }

  // Another implementation of `factorial`, this time with a `while` loop
  def factorialLoop(n: Int): Int = {
    var acc = 1
    var i = n
    while (i > 0) { acc *= i; i -= 1 }
    acc
  }

  // fibo: head-recursive
  def fibHR(n: Int): Int = n match {
    case n if n < 1  => throw new RuntimeException("fibo isn't defined on numbers < 1")
    case n if n <= 2 => 1
    case _           => fibHR(n-1) + fibHR(n-2)
  }

  // fibo: tail-recursive part
  def fibTR(n1: Int, n2: Int, nth: Int): Int = nth match {
    case 1 => n1
    case _ => fibTR(n2, n2+n1, nth-1)
  }

  // fibo: tail-recursive runner
  def fib(n: Int): Int =
    fibTR(1, 1, n)

  // This definition and `formatAbs` are very similar..
  private def formatFactorial(n: Int) = {
    val msg = "The factorial of %d is %d."
    msg.format(n, factorial(n))
  }

  // We can generalize `formatAbs` and `formatFactorial` to
  // accept a _function_ as a parameter
  def formatResult(name: String, n: Int, f: Int => Int) =
  s"The $name of $n is ${f(n)}."

}

object AnonymousFunctions {
  import Chapter2._

  // Some examples of anonymous functions:
  def main(args: Array[String]): Unit = {
    println(formatResult("absolute value", -42, abs))
    println(formatResult("factorial", 7, factorial))
    println(formatResult("increment", 7, (x: Int) => x + 1))
    println(formatResult("increment2", 7, (x) => x + 1))
    println(formatResult("increment3", 7, x => x + 1))
    println(formatResult("increment4", 7, _ + 1))
    println(formatResult("increment5", 7, x => { val r = x + 1; r }))
  }
}

object MonomorphicBinarySearch {

  def binarySearch(ds: Array[Double], key: Double): Int = {
    @annotation.tailrec
    def go(low: Int, high: Int): Int = {
      if (low > high) -low
      else {
        val mid = (low + high) / 2
        val m = ds(mid)
        if      (key > m) go(mid + 1, high   ) // go right
        else if (key < m) go(low,     mid - 1) // go left
        else mid
      }
    }
    go(0, ds.length - 1)
  }

}

object PolymorphicFunctions {

  def binarySearch[A](as: Array[A], key: A, gt: (A,A) => Boolean): Int = {
    @annotation.tailrec
    def go(low: Int, high: Int): Int = {
      if (low > high) -low // position to insert
      else {
        val mid2 = (low + high) / 2
        val a = as(mid2)
        if      (gt(a, key)) go(low,      mid2 - 1)
        else if (gt(key, a)) go(mid2 + 1, high    )
        else mid2
      }
    }
    go(0, as.length - 1)
  }

  def isSorted[A](as: Array[A], gt: (A,A) => Boolean): Boolean = {
    @tailrec
    def go(idx: Int): Boolean =
      if (idx >= as.length-1) true
      else
        if (gt(as(idx), as(idx+1))) false
        else go(idx+1)
    go(0)
  }

  def partial1[A,B,C](a: A, f: (A,B) => C): B => C =
    (b: B) => f(a, b)

  def curry[A,B,C](f: (A, B) => C): A => (B => C) =
    (a: A) => (b: B) => f(a, b)

  def uncurry[A,B,C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  def compose[A,B,C](f: A => B, g: B => C): A => C =
    (a: A) => g(f(a))

}
