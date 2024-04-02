package cats101.c101eval

import cats.Eval

object C107EvalDeferTrampoline extends App {

  /** recursive, plain */
  def factorial1(n: BigInt): BigInt =
    if (n == 1) 1
    else factorial1(n - 1) * n

  /** recursive, with Eval */
  def factorial2(n: BigInt): Eval[BigInt] =
    if (n == 1) Eval.now(n)
    else factorial2(n - 1).map(_ * n)

  /** trampolined, recursion w.o stack frame consumption, this recursion will be put to the heap */
  def factorial3(n: BigInt): Eval[BigInt] =
    if (n == 1) Eval.now(n)
    else Eval.defer { factorial3(n - 1).map(p => p * n) }

  val n = 50000
  lazy val f1 = factorial1(n)       // will blow on access
  lazy val f2 = factorial2(n).value // will blow on access
  val f3 = factorial3(n).value      // works fine

}
