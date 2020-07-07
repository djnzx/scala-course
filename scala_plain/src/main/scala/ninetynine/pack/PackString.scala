package ninetynine.pack

import scala.annotation.tailrec

object PackString {
  type Item = (Char, Int)

  /**
    * classic FP match-based solution
    */
  object ClassicFP {
    @tailrec
    def go1(tail: List[Char], buf: List[Char], acc: List[Item]): List[Item] =
      (tail, buf) match {
        case (Nil,  Nil)            =>                  acc               // empty tail, empty buffer
        case (Nil,  c::_)           => (c, buf.size) :: acc               // empty tail, NON-empty buffer
        case (h::t, Nil)            => go1(t, h::Nil, acc)                // NON-empty tail, empty buffer
        case (h::t, c::_) if h == c => go1(t, h::buf, acc)                // NON-empty tail, NON-empty buffer, same char  
        case (h::t, c::_)           => go1(t, h::Nil, (c, buf.size)::acc) // NON-empty tail, NON-empty buffer, different char
      }

    def pack_string_classic(s: String): List[Item] =
      go1(s.toList, Nil, Nil).reverse

    implicit class PackStringClassic(s: String) {
      def pack: List[Item] = pack_string_classic(s)
    }
  }

}
