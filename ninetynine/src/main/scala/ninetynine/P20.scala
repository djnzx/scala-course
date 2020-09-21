package ninetynine

import scala.annotation.tailrec

object P20 {
  def deleteAt(n: Int, xs: List[Symbol]): (List[Symbol], Symbol) = {

    @tailrec
    def go(cnt: Int, xs: List[Symbol], acc: List[Symbol]): (List[Symbol], Symbol) = xs match {
      case h::t => if (cnt < n) go(cnt + 1, t, h::acc) else (acc.reverse ++ t, h)
      case Nil => ???
    }

    go(0, xs, Nil)
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = deleteAt(3, source)
    println(s"Actual1: $actual")
  }
}
