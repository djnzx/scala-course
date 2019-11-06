package x060essential

import scala.annotation.tailrec

/**
  * structural recursion
  */
object X128 extends App {
  sealed trait IntList {
    def abst_non_tr(end: Int, func: (Int, Int) => Int): Int = this match {
      case End => end
      case Pair(hd, tl) => func(hd, tl.abst_non_tr(end, func))
    }

    @tailrec
    final def abst_tl(acc: Int, func: (Int, Int) => Int): Int = this match {
      case End => acc
      case Pair(hd, tl) => tl.abst_tl(func(acc, hd), func)
    }

    // was
    def length1: Int = this match {
      case End => 0
      case Pair(_, tail) => 1 + tail.length1
    }
    // now non-TR
    def length2: Int = abst_non_tr(0, (_, b) => b + 1)
    // now TR
    def length3: Int = abst_tl(0, (a, _) => a + 1)

    // was
    def sum1: Int = this match {
      case End => 0
      case Pair(hd, tl) => hd + tl.sum1
    }
    // now non-TR
    def sum2: Int = abst_non_tr(0, _ + _ )
    // now TR
    def sum3: Int = abst_tl(0, _ + _)
    // was
    def product1: Int = this match {
      case End => 1
      case Pair(head, tail) => head * tail.product1
    }
    // now non-TR
    def product2: Int = abst_non_tr(1, _ * _ )
    // now TR
    def product3: Int = abst_tl(1, _ * _ )

    def double: IntList = this match {
      case End => End
      case Pair(head, tail) => Pair(head * 2, tail.double)
    }
  }
  case object End extends IntList
  final case class Pair(head: Int, tail: IntList) extends IntList

  object add1 {
    def apply(n: Int): Int = n + 1
  }

//  println(add1(10))
  val example = Pair(2, Pair(2, Pair(3, Pair(5, End))))
  assert(example.sum1 == 12)
  assert(example.sum2 == 12)
  assert(example.sum3 == 12)
  assert(example.product1 == 60)
  assert(example.product2 == 60)
  assert(example.product3 == 60)
  assert(example.length1 == 4)
  assert(example.length2 == 4)
  assert(example.length3 == 4)
}
