package ninetynine

import scala.annotation.tailrec

object P08 {
  
  def compress(xs: List[Symbol]): List[Symbol] = {
    @tailrec
    def compress(xs: List[Symbol], prev: Symbol, acc: List[Symbol]): List[Symbol] = (xs, prev) match {
      case (Nil, _)  => acc
      case (h::t, p) => if (h == p) compress(t, p, acc) else compress(t, h, acc :+ h)
    }
    val h::t = xs
    compress(t, h, List(h))
  }

  def test(): Unit = {
    val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(data)
    val r = compress(data)
    println(r)
  }
  
}
