package x060essential

/**
  * structural recursion
  */
object X128 extends App {
  sealed trait IntList {
    def abstraction(end: Int, func: (Int, Int) => Int): Int = this match {
      case End => end
      case Pair(hd, tl) => func(hd, tl.abstraction(end, func))
    }

    def length: Int = this match {
      case End => 0
      case Pair(_, tail) => 1 + tail.length
    }
    def sum: Int = this match {
      case End => 0
      case Pair(hd, tl) => hd + tl.sum
    }
    def product: Int = this match {
      case End => 1
      case Pair(head, tail) => head * tail.product
    }
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

  print(add1(10))
}
