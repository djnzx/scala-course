package ninetynine

import scala.annotation.tailrec

object P18TR {
  
  def sliceAt(i: Int, k: Int, xs: List[Symbol]): List[Symbol] = {
    @tailrec
    def go(c: Int, tail: List[Symbol], acc: List[Symbol]): List[Symbol] = tail match {
      case h :: t => if (c < i)      go(c+1, t, acc)
      else if (c < k) go(c+1, t, h::acc)
      else acc
      case Nil => ???
    }

    go(0, xs, Nil).reverse
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = sliceAt(3, 7, source) //List('d, 'e, 'f, 'g)
    println(s"Actual: $actual")
  }
  
}
