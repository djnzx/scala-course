package ninetynine

import scala.annotation.tailrec

object P14TR {
  
  @tailrec
  def duplicate(xs: List[Symbol], acc: List[Symbol]): List[Symbol] = xs match {
    case Nil    => acc
    case h :: t => duplicate(t, h::h::acc)
  }

  def test(): Unit = {
    val source = List('x, 'a, 'b, 'c, 'a, 'd, 'e)
    println(s"Source: $source")
    val actual = duplicate(source, Nil).reverse
    println(s"Actual: $actual")
  }
  
}
