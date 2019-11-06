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

    // was
    def length: Int = this match {
      case End => 0
      case Pair(_, tail) => 1 + tail.length
    }
    // now
    def length2: Int = abstraction(0, (_, b) => b + 1)

    // was
    def sum: Int = this match {
      case End => 0
      case Pair(hd, tl) => hd + tl.sum
    }
    // now
    def sum2: Int = abstraction(0, _ + _ )

    // was
    def product: Int = this match {
      case End => 1
      case Pair(head, tail) => head * tail.product
    }
    // now
    def product2: Int = abstraction(1, _ * _ )

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

  println(add1(10))
  val example = Pair(1, Pair(2, Pair(3, Pair(4, End))))
  assert(example.sum == 10)
  assert(example.sum2 == 10)
  assert(example.product == 24)
  assert(example.product2 == 24)
  assert(example.length == 4)
  assert(example.length2 == 4)
}
