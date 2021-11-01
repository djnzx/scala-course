package ninetynine

import tools.spec.ASpec

/** Group the elements of a set into disjoint subsets. multinomial coefficients
  */
object P27 {
  import P26._
  type L[A] = List[A]
  type LL[A] = List[List[A]]
  type LLL[A] = List[List[List[A]]]

  implicit class MyListOps[A](private val me: List[A]) extends AnyVal {
    def --(another: List[A]): List[A] = me.filter(!another.contains(_))
  }

  def group3[A](la: List[A]) =
    for {
      a <- combinations(2, la) // groups of 2
      woA = la -- a
      b <- combinations(3, woA) // groups of 3 w.o original ones
      woAB = woA -- b // residual. if group of 6, this will be group of1
    } yield List(a, b, woAB)

  //                                         variants
  //                                              groups
  //                                                   group
  def group[A](ns: List[Int], la: List[A]): LLL[A] = {
    require(ns.sum == la.length, "Sum of the list should equal to number of the list items")

    ns match {
      case Nil => List(Nil)
      case n :: ns =>
        combinations(n, la) // possible groups of N items
          .flatMap { c: List[A] => // c       - current subgroup
            group(ns, la -- c) // la -- c - rest of the team, build recursively
              .map { c :: _ } // attach the head
          }
    }
  }

  def groupA[A](ns: List[Int], la: L[A]): LLL[A] = ns match {
    case Nil => List(Nil)
    case n :: ns =>
      for {
        c <- combinations(n, la)
        g <- groupA(ns, la -- c).map { c :: _ }
      } yield g
  }

}

class P27Spec extends ASpec {
  import P27._

  it("1") {
    groupA(List(2, 2), "abcd".toList).foreach(println)
  }

  it("3") {
    group3("abcdef".toList).foreach(println)
  }
}
