package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/special-palindrome-again/problem
  * 14/17 test cases failed :(
  */
object SpecialStringAgainApp extends App {
  val oneToN = (n: Long) => n*(n+1)/2
  final case class Item(ch: Char, cnt: Long)
  final case class Res(l: List[Item], buf: List[Char])
  def substrCount(n: Int, origin: String): Long = {
    val rx: Res = origin.foldLeft(Res(Nil, Nil)) { (r, c) => r match {
      case Res(list, Nil)         => Res(list, c::Nil)
      case Res(list, h::t)       =>
        if (c==h)                    Res(list, c::h::t)
        else                         Res(Item(h, r.buf.size)::list, c::Nil)
    }}
    val ls = Item(rx.buf.head, rx.buf.size)::rx.l
    println(ls)
    def fold(tl: List[Item], acc: Long): Long = {
      println(s"$tl : $acc")
      tl match {
        case Nil             => acc
        case h::Nil          => fold(Nil,    acc + oneToN(h.cnt))
        case h::t::Nil       => fold(t::Nil, acc + oneToN(h.cnt))
        case l::c::r::tail   =>
          if (l==r && c.cnt==1) fold(c::r::tail, acc + oneToN(l.cnt) + l.cnt)
          else                  fold(c::r::tail, acc + oneToN(l.cnt))
      }
    }
    fold(ls, 0)
  }
  val s = "aabaa"
  println(substrCount(s.length, s))
}
