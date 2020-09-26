package ninetynine

import scala.annotation.tailrec

/**
  * Pack consecutive duplicates of list elements into sub-lists
  */
object P09 {

  def pack[A](xs: List[A]): List[List[A]] = {

    @tailrec
    def pack(xs: List[A], buf: List[A], acc: List[List[A]]): List[List[A]] = (xs, buf) match {
      case (Nil, Nil)                  => Nil                             // first step, EMPTY given
      case (Nil, _)                    => acc :+ buf                      // last step
      case (xh::xt, Nil)               => pack(xt, List(xh), acc )        // first step, NON-EMPTY given
      case (xh::xt, bh::_) if xh == bh => pack(xt, xh::buf, acc)          // same char
      case (xh::xt, bh::_) if xh != bh => pack(xt, List(xh), acc :+ buf)  // different char
    }

   pack(xs, List.empty, List.empty)
  }

}

class P09Spec extends NNSpec {
  import P09._
  
  it("1") {
    val data = Vector(
      "" -> List(),
      "A" -> List(List('A')),
      "AA" -> List(List('A', 'A')),
      "AAA" -> List(List('A', 'A', 'A')),
      "AAB" -> List(List('A', 'A'), List('B')),
      "AABB" -> List(List('A', 'A'), List('B', 'B')),
    )
    
    for {
      (in, out) <- data
    } pack(in.toList) shouldEqual out
  }
}
