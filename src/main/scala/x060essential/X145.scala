package x060essential

object X145 extends App {
  sealed trait LinkedList[A] {
    def map[B](fn: A => B): LinkedList[B] = this match {
      case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))
      case End() => End[B]()
    }
  }
  final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
  final case class End[A]() extends LinkedList[A]

  val list: LinkedList[Int] = Pair(1, Pair(2, Pair(3, End())))
  val list2: LinkedList[Int] = list.map(_ + 1)
  println(list)
  println(list2)
}
