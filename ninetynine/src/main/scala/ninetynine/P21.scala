package ninetynine

import scala.annotation.tailrec

/**
  * Insert an element at a given position into a list
  */
object P21 {
  def insertAt(c: Char, n: Int, xs: List[Char]) = {

    @tailrec
    def doIt(cnt: Int, xs: List[Char], acc: List[Char]): List[Char] = xs match {
      case h::t if cnt < n  => doIt(cnt + 1, t, h::acc)
      case h::t if cnt == n => (h :: c :: acc).reverse ::: t
      case Nil  if cnt == n => (c :: acc) reverse
      case Nil              => throw new NoSuchElementException
    }

    doIt(0, xs, Nil)
  }

}

class P21Spec extends NNSpec {
  import P21._
  
  it("1") {
    val data = Seq(
      ("ABC", 0, 'X') -> "XABC",
      ("ABC", 1, 'X') -> "AXBC",
      ("ABC", 2, 'X') -> "ABXC",
      ("ABC", 3, 'X') -> "ABCX",
    )
    val datax = Seq(
      ("AB", 3, 'X')
    )
    for {
      ((into, at, what), out) <- data
    } insertAt(what, at, into.toList).mkString shouldEqual out
    
    for {
      (into, at, what) <- datax
    } an[NoSuchElementException] should be thrownBy insertAt(what, at, into.toList)
  }
}
