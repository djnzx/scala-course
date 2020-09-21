package ninetynine

import scala.annotation.tailrec

object P15TR {
  
  def nTimes(n: Int, sym: Symbol): Seq[Symbol] = 1 to n map { _ => sym } toList

  @tailrec
  def duplicate(n: Int, xs: List[Symbol], acc: List[Symbol]): List[Symbol] = xs match {
    case Nil    => acc
    case h :: t => duplicate(n, t, acc ++ nTimes(n, h))
  }

  def test(): Unit = {
    val source = List('x, 'a, 'b, 'c, 'a, 'd, 'e)
    println(s"Source: $source")
    val actual = duplicate(3, source, Nil)
    println(s"Actual: $actual")
  }
  
}
