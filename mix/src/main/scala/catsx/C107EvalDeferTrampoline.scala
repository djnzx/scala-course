package catsx

import cats.Eval

object C107EvalDeferTrampoline extends App {

  // recursive 1
  def factorial1(n: BigInt): BigInt =
    if (n==1) 1
    else factorial1(n - 1) * n

  // recursive 2
  def factorial2(n: BigInt): Eval[BigInt] =
    if (n==1) Eval.now(n)
    else factorial2(n - 1).map(_ * n)

  // trampolined, recursion w.o stack frame consumption
  def factorial(n: BigInt): Eval[BigInt] =
    if (n==1) Eval.now(n)
    else Eval.defer(factorial(n - 1).map(_ * n))

  println(factorial(50000).value)

}
