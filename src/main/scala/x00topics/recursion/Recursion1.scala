package x00topics.recursion

import scala.annotation.tailrec

object Recursion1 extends App {
  val list = 1 to 1_000_000 toList

  // this function can't be optimized for tail recursion
  private def lenr[A](list: List[A]): Int = list match {
    case Nil => 0
    case _ :: tail => 1 + lenr(tail)
  }

  // this function can be optimized for tail recursion
  @tailrec
  private def lenar[A](list: List[A], ac: Int): Int = list match {
    case Nil => ac
    case _ :: tail => lenar(tail, ac + 1)
  }

  def len[A](list: List[A]): Int = {
    lenar(list, 0);
  }

  //println(list)
  println(len(list))
}
