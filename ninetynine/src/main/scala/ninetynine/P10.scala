package ninetynine

import scala.annotation.tailrec

/**
  * Run-length encoding of a list
  */
object P10 {

  def pack[A](xs: List[A]): List[(A, Int)] = {

    @tailrec
    def pack(xs: List[A], tmp: Option[(A, Int)], acc: List[(A, Int)]): List[(A, Int)] = (xs, tmp) match {
      case (Nil,    None)                      => acc
      case (Nil,    Some(t @ _))               => t :: acc
      case (xh::xt, None)                      => pack(xt, Some(xh, 1), acc)
      case (xh::xt, Some((c, cnt))) if xh == c => pack(xt, Some(c, cnt + 1), acc)      // the same letter, keep counting
      case (xh::xt, Some(t @ _))               => pack(xt, Some(xh, 1)     , t :: acc) // the letter is different, start counting from 1
    }

    pack(xs, None, Nil) reverse
  }

}

class P10Spec extends NNSpec {
  import P10._
  
  it("1") {
    val data = Vector(
      "" -> List.empty,
      "A" -> List(('A',1)),
      "AA" -> List(('A',2)),
      "AAB" -> List(('A',2), ('B',1)),
      "AABB" -> List(('A',2), ('B',2)),
    )
    
    for {
      (in, out) <- data
    } pack(in.toList) shouldEqual out
    
  }
}
