package essential

object X132 extends App {
  sealed trait LinkedList[A] {
    def fold[B](end: B)(f: (A, B) => B): B =
      this match {
        case End() => end
        case Pair(hd, tl) => f(hd, tl.fold(end)(f))
      }
  }
  final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
  final case class End[A]() extends LinkedList[A]

  object Sum {
    def sum(x: Int, y: Int) = x + y
  }
  // converting method to function
  val f = Sum.sum _



}
