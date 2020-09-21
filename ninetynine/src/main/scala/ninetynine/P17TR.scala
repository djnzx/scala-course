package ninetynine

import scala.annotation.tailrec

object P17TR {
  
  def splitAt(n: Int, xs: List[Symbol]): (List[Symbol], List[Symbol]) = {
    @tailrec
    def moveAndCount(n: Int, tail: List[Symbol], acc: List[Symbol]): (List[Symbol], List[Symbol]) = tail match {
      case h :: t =>
        if (n == 0) (acc, tail)
        else moveAndCount(n-1, t, h::acc)
      case _    => ???
    }
    val (l, r) = moveAndCount(n, xs, Nil)
    (l.reverse, r)
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = splitAt(3, source)
    println(s"Actual: $actual")
  }
  
}
