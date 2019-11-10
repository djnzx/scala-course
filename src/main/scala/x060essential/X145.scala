package x060essential

object X145 extends App {
  sealed trait LinkedList[A] {
    def map[B](fn: A => B): LinkedList[B] = this match {
      case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))
      case End() => End[B]()
    }
    def flatMap[B](fn: A => LinkedList[B]): LinkedList[B] = this match  {
      case Pair(hd, tl) => {
        val hd2: LinkedList[B] = fn(hd)
        val tl2: LinkedList[B] = tl.flatMap[B](fn)
        var list = hd2
        for {
          tl2i <- tl2
        } yield list = Pair(tl2i, list)
        list
      }
      case End() => End[B]()
    }
  }
  final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
  final case class End[A]() extends LinkedList[A]

  val list: LinkedList[Int] = Pair(1, Pair(2, Pair(3, End())))
  val list2: LinkedList[Int] = list.map(_ + 1)
  val list3: LinkedList[Int] = list.flatMap(v => Pair(-v, Pair(v, End())))
  println(list)
  println(list2)
  println(list3)
}
