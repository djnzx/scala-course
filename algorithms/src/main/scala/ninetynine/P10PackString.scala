package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * Run-length encoding of a String
  */
object P10PackString {
  type Item = (Char, Int)

  @tailrec
  private def go(tail: List[Char], buf: List[Char], acc: List[Item]): List[Item] =
    (tail, buf) match {
      case (Nil,  Nil)            =>                  acc              // empty tail, empty buffer
      case (Nil,  c::_)           => (c, buf.size) :: acc              // empty tail, NON-empty buffer
      case (h::t, Nil)            => go(t, h::Nil, acc)                // NON-empty tail, empty buffer
      case (h::t, c::_) if h == c => go(t, h::buf, acc)                // NON-empty tail, NON-empty buffer, same char  
      case (h::t, c::_) if h != c => go(t, h::Nil, (c, buf.size)::acc) // NON-empty tail, NON-empty buffer, different char
    }

  private def pack_string_classic(s: String): List[Item] =
    go(s.toList, Nil, Nil).reverse

  implicit class PackStringClassic(s: String) {
    def pack: List[Item] = pack_string_classic(s)
  }

}

class P10PackStringSpec extends ASpec {
  import P10PackString.PackStringClassic

  val dataset = Vector(
    ""          -> List(),
    "A"         -> List('A'->1),
    "AB"        -> List('A'->1, 'B'->1),
    "ABB"       -> List('A'->1, 'B'->2),
    "ABBC"      -> List('A'->1, 'B'->2, 'C'->1),
    "ABBCDDD"   -> List('A'->1, 'B'->2, 'C'->1, 'D'->3),
    "ABBCCCDDE" -> List('A'->1, 'B'->2, 'C'->3, 'D'->2, 'E'->1),
  )

  it("6x") {
    for {
      (input, exp) <- dataset
    } input.pack shouldEqual exp
  }
  
}
