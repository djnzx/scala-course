package catsx.c101eval

import cats.Eval

object C108SafeFoldWithDefer extends App {

  /**
    * scala library implementation
    * fold = foldLeft means fold from Left to Right
    * fp implementation:
    * foldLeft  means: from Right to left ( <-- )
    * foldRight means: from Left to Right ( --> )
    */

  /**
    * O(N)
    * classic non-tail-recursive solution
    * not stack-safe
    */
  def foldRightNTR[A, B](as: List[A], acc: B)(f: (A, B) => B): B = as match {
    case Nil => acc
    case h :: t =>
      // recursive list decompose
      val b_from_tail: B = foldRightNTR(t, acc)(f)
      // applying function on the back way
      f(h, b_from_tail)
  }

  /**
    * tail-recursive solution
    * but tail recursive implementation can't be generalized
    */
  def foldRightTR[A, B](as: List[A], acc: B)(f: (A, B) => B): B = as match {
    case Nil => acc
    case h :: t =>
      // apply function to the first element (head)
      val newAcc: B = f(h, acc)
      // running the same folding by applying to the residual elements (tail) and new accumulator
      foldRightTR(t, newAcc)(f)
  }

  // classic non-tail-recursive solution based on EVAL
  def foldRightEval[A, B](as: List[A], acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = as match {
    case Nil => acc
    case h :: t => Eval.defer { f(h, foldRightEval(t, acc)(f)) }
  }

  /**
    * syntax 1
    * mapping
    * f: (A, B) => B
    * to
    * g: (A, Eval[B]) => Eval[B]
    */
  def fToEval[A, B](f: (A, B) => B): (A, Eval[B]) => Eval[B] =
    (a: A, eb: Eval[B]) => eb.map(b => f(a, b))

  def foldRightViaEval[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc))(fToEval(f)).value

  def foldRightViaEval2[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc))((a, eb) => eb.map(b => f(a, b))).value

  val gt0 = (a: Int, b: Boolean) => b && (a > 0)
  val gt1 = (a: Int, b: Boolean) => b && (a > 1)
  val acc0 = true

  val data = (1 to 10_000).toList
  // works, because of tail recursion
  println(foldRightTR(data, acc0)(gt0)) // true
  println(foldRightTR(data, acc0)(gt1)) // false

  // works, because of Eval.defer
  println(foldRightViaEval(data, acc0)(gt0)) // true
  println(foldRightViaEval(data, acc0)(gt1)) // false

  // will break in runtime (StackOverflowError)
  //  println(foldRightNTR(data, acc0)(f0)) // true
  //  println(foldRightNTR(data, acc0)(f1)) // false

}
