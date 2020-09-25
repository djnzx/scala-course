package ninetynine

import scala.annotation.tailrec

/**
  * Modified run-length encoding
  */
object P11 {

  def rle[A](xs: List[A]) = {

    @tailrec
    def pack(xs: List[A], tmp: Option[(A, Int)], acc: List[Any]): List[Any] =
      (xs, tmp) match {
        case (Nil,    None)                      => acc
        case (Nil,    Some((c, 1)))              => c :: acc
        case (Nil,    Some(t @ _))               => t :: acc
        case (xh::xt, None)                      => pack(xt, Some(xh, 1),      acc)
        case (xh::xt, Some((c, cnt))) if xh == c => pack(xt, Some(c, cnt + 1), acc)      // the same letter, keep counting
        case (xh::xt, Some((c, 1)))              => pack(xt, Some(xh, 1)     , c :: acc) // the letter is different, start counting from 1
        case (xh::xt, Some(t @ _))               => pack(xt, Some(xh, 1)     , t :: acc) // the letter is different, start counting from 1
      }
    pack(xs, None, Nil) reverse
  }

}

class P11Spec extends NNSpec {
  import P11._

  it("1") {
    val data = Vector(
      "" -> List.empty,
      "A" -> List('A'),
      "AA" -> List(('A',2)),
      "AAB" -> List(('A',2), 'B'),
      "AABB" -> List(('A',2), ('B',2)),
    )
    
    for {
      (in, out) <- data
    } yield rle(in.toList) shouldEqual out
    
  }
}
