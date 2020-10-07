package geeksforgeeks.p1basic

import tools.spec.ASpec

object B000Factorial {

  /**
    * recursive, mathematical definition
    * complexity - exponential
    */
  def fact_r(n: Int): Int = n match {
    case 0 => 1
    case n => n * fact_r(n-1) 
  }

  /**
    * tail recursive with accumulator
    * actually iterative
    * complexity - linear
    */
  def fact_tr(n: Int, acc: Int = 1): Int = n match {
    case 0 => acc
    case n => fact_tr(n-1, n * acc)
  }

  /**
    * iterative approach
    * complexity - linear
    */
  def fact_iter(n: Int) =
    (1 to n).foldLeft(1)(_*_)

  def fact_builtin(n: Int) =
    (1 to n).product
  
}

class B000FactorialSpec extends ASpec {
  import B000Factorial._
  
  it("1") {
    val data = Seq(
      0 -> 1,
      1 -> 1,
      2 -> 2,
      3 -> 6,
      4 -> 24,
      5 -> 120,
      6 -> 720,
    )

    val impls: Seq[Int => Int] = Seq(
      fact_r,
      (n: Int) => fact_tr(n),
      fact_iter,
      fact_builtin,
    )

    runAllS(data, impls)
  }
  
}