package ninetynine

import scala.annotation.tailrec

object P16TR {
  
  def dropNth(n: Int, xs: List[Symbol]): List[Symbol] = {
    @tailrec
    def dropNth(n: Int, cnt: Int, xs: List[Symbol], acc: List[Symbol]): List[Symbol] = xs match {
      case Nil    => acc
      case h :: t =>
        if (n == cnt) dropNth(n, 1, t, acc)
        else          dropNth(n, cnt + 1, t, h::acc)
    }
    dropNth(n, 1, xs, Nil) reverse
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = dropNth(3, source)
    println(s"Actual: $actual")
  }
  
}
