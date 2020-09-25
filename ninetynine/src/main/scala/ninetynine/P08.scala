package ninetynine

import scala.annotation.tailrec

/**
  * Eliminate consecutive duplicates of list elements
  */
object P08 {
  
  def compress(xs: List[Char]): List[Char] = {
    
    @tailrec
    def compress(xs: List[Char], buf: List[Char], acc: List[Char]): List[Char] = (xs, buf) match {
      case (Nil, Nil)             => Nil                          // first step,     empty list
      case (h::t, Nil)            => compress(t, List(h), acc)    // first step, NON-empty list
      case (Nil, c::_)            => c::acc                       // last iteration, just add the buffer
      case (h::t, c::_) if c == h => compress(t, List(h), acc)    // non-last, skip the same char
      case (h::t, c::_) if c != h => compress(t, List(h), c::acc) // non-last, different, collect char
    }
    
    compress(xs, List.empty, List.empty) reverse
  }

}

class P08Spec extends NNSpec {
  import P08._
  
  it("1") {
    Map(
      "" ->"",
      "a" -> "a",
      "aa" -> "a",
      "aab" -> "ab",
      "aabb" -> "ab",
      "aabba" -> "aba",
    ).foreach { case (data, exp) =>
      compress(data.toList) shouldEqual exp.toList
    }
  }
}
