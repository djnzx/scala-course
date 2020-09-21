package ninetynine

import scala.annotation.tailrec

object P09 {

  def pack[A](xs: List[A]): List[List[A]] = {

    @tailrec
    def pack(xs: List[A], tmp: List[A], acc: List[List[A]]): List[List[A]] = (xs, tmp) match {
      case (Nil, _)  => acc :+ tmp
      case (xh::xt, th::_) => if (xh == th) pack(xt, xh::tmp, acc) else pack(xt, List(xh), acc :+ tmp)
    }

    val h::t = xs
    pack(t, List(h), Nil)
  }

  def test(): Unit = {
    val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(data)
    val r = pack(data)
    println(r)
  }
  
}
