package x060essential

object X121 extends App {
  sealed trait Result[A]
  final case class Success[A](result: A) extends Result[A]
  final case class Failure[A](reason: String) extends Result[A]

  sealed trait LinkedList[A] {
    def length: Int = this match {
      case End() => 0
      case Pair(_, tl) => 1 + tl.length
    }
    def contains(item: A): Boolean = this match {
      case End() => false
      case Pair(head, tail) => if (head == item) true else tail.contains(item)
    }
    def apply(index: Int): Result[A] = this match {
      case End() => Failure(s"Requested index out of range")
      case Pair(hd, tl) => if (index == 0) Success(hd) else tl(index - 1)
    }
//    def double: LinkedList =
//      this match {
//        case End => End
//        case Pair(hd, tl) => Pair(hd * 2, tl.double)
//      }
//    def product: Int =
//      this match {
//        case End => 1
//        case Pair(hd, tl) => hd * tl.product
//      }
//    def sum: Int =
//      this match {
//        case End => 0
//        case Pair(hd, tl) => hd + tl.sum }
  }
  final case class End[A]() extends LinkedList[A]
  final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]

  val example = Pair(1, Pair(2, Pair(3, End())))
  assert(example.length == 3)
  assert(example.tail.length == 2)
  assert(End().length == 0)

  assert(example.contains(3) == true)
  assert(example.contains(4) == false)
  assert(End().contains(0) == false)

  assert(example(0) == Success(1))
  assert(example(1) == Success(2))
  assert(example(2) == Success(3))
  assert(example(3) == Failure("Requested index out of range"))
//  assert(try {
//    example(3)
//    false
//  } catch {
//    case e: Exception => true
//  })
}
