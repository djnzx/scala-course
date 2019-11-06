package x060essential

import scala.annotation.tailrec

/**
  * structural recursion
  */
object X128 extends App {
  sealed trait IntList {
    // change return type from Int to A
    def fold_non_tr[A](end: A, func: (Int, A) => A): A = this match {
      case End => end
      case Pair(hd, tl) => func(hd, tl.fold_non_tr(end, func))
    }

    @tailrec
    final def fold(acc: Int, func: (Int, Int) => Int): Int = this match {
      case End => acc
      case Pair(hd, tl) => tl.fold(func(acc, hd), func)
    }

    // was
    def length1: Int = this match {
      case End => 0
      case Pair(_, tail) => 1 + tail.length1
    }
    // now non-TR
    def length2: Int = fold_non_tr[Int](0, (_, b) => b + 1)
    // now TR
    def length3: Int = fold(0, (a, _) => a + 1)

    // was
    def sum1: Int = this match {
      case End => 0
      case Pair(hd, tl) => hd + tl.sum1
    }
    // now non-TR
    def sum2: Int = fold_non_tr[Int](0, _ + _ )
    // now TR
    def sum3: Int = fold(0, _ + _)
    // was
    def product1: Int = this match {
      case End => 1
      case Pair(head, tail) => head * tail.product1
    }
    // now non-TR
    def product2: Int = fold_non_tr[Int](1, _ * _ )
    // now TR
    def product3: Int = fold(1, _ * _ )

    // was
    def double1: IntList = this match {
      case End => End
      case Pair(head, tail) => Pair(head * 2, tail.double1)
    }
    // now non-TR
    def double2: IntList = fold_non_tr[IntList](End, (head, tail) => Pair(head * 2, tail))

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
  println(example)          // Pair(2,Pair(2,Pair(3,Pair(5,End))))
  println(example.double1)  // Pair(4,Pair(4,Pair(6,Pair(10,End))))
  println(example.double2)  // Pair(4,Pair(4,Pair(6,Pair(10,End))))
}
