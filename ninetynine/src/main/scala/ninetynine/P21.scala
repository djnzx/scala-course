package ninetynine

import scala.annotation.tailrec

object P21 {
  def insertAt(el: Symbol, n: Int, xs: List[Symbol]): (List[Symbol], Symbol) = {

    @tailrec
    def go(el: Symbol, cnt: Int, xs: List[Symbol], acc: List[Symbol]): (List[Symbol], Symbol) = xs match {
      case h::t =>
        if (cnt < n) go(el, cnt + 1, t, h::acc)
        else ( (el::acc).reverse ++ t, h)
      case Nil => ???
    }

    go(el, 0, xs, Nil)
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = insertAt('new, 3, source)
    println(s"Actual1: $actual")
  }
}
