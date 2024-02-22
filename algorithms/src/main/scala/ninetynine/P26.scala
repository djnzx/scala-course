package ninetynine

/** Generate the combinations of K distinct objects chosen from the N elements of a list
  * {{{
  *     n!
  * -----------
  * k! * (n-k)!
  *
  * 3 of 4 = 4
  * 3 of 5 = 10
  *
  *     5!
  * ----------- = 10
  * 3! * (5-3)!
  * }}}
  * actual signature should be {{{Seq[A] => Seq[Set[A]]}}}
  *
  * [[https://aperiodic.net/phil/scala/s-99/#p26]]
  */
object P26 {

  private def make[A](as: List[A])(f: List[A] => List[List[A]]): List[List[A]] = as match {
    case Nil      => List.empty
    case _ :: ast =>
      val wX = f(as)
      val woX = make(ast)(f)
      wX ::: woX
  }

  private def f[A](n: Int)(as: List[A]): List[List[A]] = as match {
    case Nil      => Nil
    case h :: ast => pickN2(n, ast).map(xs => h :: xs)
  }

  def pickN2[A](n: Int, as: List[A]): List[List[A]] = n match {
    case 0 => List(List.empty)
    case n => make(as) { f(n - 1) }
  }

  // combinations w/permutations
  // List(a, b) != List(b, a)
  // pick (2, abc) => ab, ac, ba, bc, ca
  def pickX[A](n: Int, xs: List[A]): List[List[A]] = n match {
    case 0 => List(List.empty) // nothing to add here
    case n =>
      xs.flatMap { x =>
        val xsNoX = xs.filter(_ != x)          // given list without that element
        val comb1 = pickX(n - 1, xsNoX)        // combinations N-1 for xs - x
        val comb1wX = comb1.map(ys => x :: ys) // with element added
        comb1wX
      }
  }

  // combinations w.o./permutations
  // List(a, b) == List(b, a)
  // pick (2, abc) => ab, ac, bc
  def pickN[A](n: Int, xs: List[A]): List[List[A]] = (n, xs) match {
    case (0, _)        => List(List.empty) // we are done, ok
    case (_, Nil)      => List()           // we are done
    case (n, x :: nox) =>
      val wX = pickN(n - 1, nox).map(ys => x :: ys)
      val woX = pickN(n, nox)
      wX ::: woX
  }

}
