package ninetynine

import tools.spec.ASpec

/**
  * Generate the combinations of K distinct objects chosen from the N elements of a list
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
  */
object P26 {
  type L[A] = List[A]
  type LL[A] = List[List[A]]
  
  def tails[A](la: List[A])(f: L[A] => LL[A]): LL[A] = la match {
    case Nil    => List.empty
    case _ :: t => f(la) ::: tails(t)(f)
  }
  
  def combinations[A](n: Int, la: L[A]): LL[A] = n match {
    case 0 => List(List.empty)
    case _ => tails(la) { case h::t =>
      val a: List[List[A]] = combinations(n - 1, t) // one item shorter on tail
      val b: List[List[A]] = a.map(la => h :: la)   // attach head to the result
      b
    }
  }
}

class P26Spec extends ASpec {
  import P26._

  it("1") {
    val data = Seq(
      (1,"abc") -> Seq("a", "b", "c"),
      (2,"abc") -> Seq("ab", "ac", "bc"),
      (3,"abc") -> Seq("abc"),
      (3,"abcd") -> Seq("abc","abd","acd","bcd"),
      (4,"abcde") -> Seq("abcd","abce","abde","acde","bcde"),
      (3,"abcde") -> Seq("abc","abd","abe","acd","ace","ade","bcd","bce","bde","cde"),
    )

    for {
      ((n, in), out) <- data
    } combinations(n, in.toList) shouldEqual out.map(_.toList)
  }

}
