package ninetynine

import scala.annotation.tailrec

/**
  * Split a list into two parts
  */
object P17 {
  
  def splitAt(at: Int, xs: List[Char]) = {

    @tailrec
    def doIt(n: Int, tail: List[Char], acc: List[Char]): (List[Char], List[Char]) = (tail, n) match {
      case (Nil, _) | 
           (_,   0)    => (acc, tail)
      case (h :: t, n) => doIt(n - 1, t, h :: acc) 
    }

    doIt(at, xs, Nil) match {
      case (l, r) => (l.reverse, r)
    }
  }

}

class P17Spec extends NNSpec {
  import P17._
  
  it("1") {
    val data = Vector(
      (1, "") -> ("", ""),
      (10, "") -> ("", ""),
      (10, "abc") -> ("abc", ""),
      (1, "abc") -> ("a", "bc"),
      (2, "abcdef") -> ("ab", "cdef"),
      (3, "abcdefghijk") -> ("abc", "defghijk"),
    )

    for {
      ((n, in), (out1, out2)) <- data
    } splitAt(n, in.toList) shouldEqual (out1.toList, out2.toList)

  }
}