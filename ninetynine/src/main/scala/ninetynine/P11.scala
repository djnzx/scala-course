package ninetynine

import scala.annotation.tailrec

object P11 {

  def pack[A](xs: List[A]) = {

    @tailrec
    def pack(xs: List[A], tmp: (A, Int), acc: List[Any]): List[Any] = (xs, tmp) match {
      case (Nil, _)  => acc :+ tmp
      case (xh::xt, (ch, cnt)) =>
        if (xh == ch) pack(xt, (ch, cnt + 1), acc)          // the same letter, keep counting
        else cnt match {
          case 1 => pack(xt, (xh, 1), acc :+ tmp._1)
          case _ => pack(xt, (xh, 1), acc :+ tmp)
        }
    }

    val h::t = xs
    pack(t, (h, 1), Nil)
  }

  def test(): Unit = {
    val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(data)
    val r = pack(data)
    println(r)
  }
  
}
