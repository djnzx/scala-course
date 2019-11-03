package x00topics.recursion

import scala.annotation.tailrec

object Recursion1 extends App {
  val list = 1 to 10_000 toList

  @tailrec
  private def lena[A](list: List[A], ac: Int): Int = list match {
    case Nil => ac
    case _ :: tail => lena(tail, ac + 1)
  }

  def len[A](list: List[A]): Int = {
    lena(list, 0);
  }

  println(list)
  println(len(list))
}
