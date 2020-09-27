package toptal

import scala.annotation.tailrec

object Task2 {
  
  def pack[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def packIt(xs: List[A], tmp: Option[(A, Int)], acc: List[(A, Int)]): List[(A, Int)] = (xs, tmp) match {
      case (Nil,    None)                      => acc
      case (Nil,    Some(t @ _))               => t :: acc
      case (xh::xt, None)                      => packIt(xt, Some(xh, 1), acc)
      case (xh::xt, Some((c, cnt))) if xh == c => packIt(xt, Some(c, cnt + 1), acc)      // the same letter, keep counting
      case (xh::xt, Some(t @ _))               => packIt(xt, Some(xh, 1)     , t :: acc) // the letter is different, start counting from 1
    }

    packIt(xs, None, Nil) reverse
  }

}
