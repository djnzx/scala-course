package ninetynine

import scala.annotation.tailrec

object P10 {

  def pack[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def pack(xs: List[A], tmp: (A, Int), acc: List[(A, Int)]): List[(A, Int)] = (xs, tmp) match {
      case (Nil, _)  => acc :+ tmp
      case (xh::xt, (ch, cnt)) =>
        if (xh == ch) pack(xt, (ch, cnt + 1), acc)        // the same letter, keep counting
        else          pack(xt, (xh, 1)      , acc :+ tmp) // the letter is different, start counting from 1
    }

    val h::t = xs
    pack(t, (h, 1), Nil)
  }

  def test(): Unit = {
    val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(data)
    val r: List[(Symbol, Int)] = pack(data)
    println(r)
  }
}
