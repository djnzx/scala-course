package ninetynine

import tools.spec.ASpec

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
  */
object P26 {

  def tails[A](as: List[A])(f: List[A] => List[List[A]]): List[List[A]] = as match {
    case Nil    => List.empty
    case _ :: t => f(as) ::: tails(t)(f)
  }

  def combinations[A](n: Int, as: List[A]): List[List[A]] = n match {
    case 0 => List(List.empty)
    case n =>
      tails(as) {
        case h :: t =>
          combinations(n - 1, t)
            .map(la => h :: la)
        case Nil => sys.error("impossible by design")
      }
  }
}

class P26Spec extends ASpec {
  import P26._

  it("1") {
    val data = Seq(
      (1, "abc") -> Seq("a", "b", "c"),
      (2, "abc") -> Seq("ab", "ac", "bc"),
      (3, "abc") -> Seq("abc"),
      (3, "abcd") -> Seq("abc", "abd", "acd", "bcd"),
      (4, "abcde") -> Seq("abcd", "abce", "abde", "acde", "bcde"),
      (3, "abcde") -> Seq("abc", "abd", "abe", "acd", "ace", "ade", "bcd", "bce", "bde", "cde"),
    )

    for {
      ((n, in), out) <- data
    } combinations(n, in.toList) shouldEqual out.map(_.toList)
  }
}

object P26Runner extends App {
  import P26._

  pprint.pprintln(combinations(2, "ABCD".toList))
  pprint.pprintln(combinations(3, "ABCDE".toList))
}
