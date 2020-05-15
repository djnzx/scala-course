package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/special-palindrome-again/problem
  * 14/17 test cases failed :(
  */
object SpecialStringAgainApp extends App {

  sealed trait Itm
  final case class Item(c: Char, cnt: Int) extends Itm
  final case object Empty extends Itm
  final case class Res(l: List[Item], max: Int, buf: Itm)

  def substrCount(n: Int, origin: String): Long = {
    val rx: Res = origin.foldLeft(Res(Nil, 0, Empty)) { (r, c) => r match {
      case Res(list, max, Empty)         => Res(list, max, Item(c, 1))
      case Res(list, max, Item(ch, cnt)) =>
        if (c==ch)                               Res(list, max, Item(c, cnt+1))
        else      Res(Item(ch, cnt)::list, math.max(max, cnt), Item(c, 1))
    }}
    val (ls, max) = (rx.buf::rx.l reverse, math.max(rx.max, rx.buf.asInstanceOf[Item].cnt))
      println(s"ls: $ls")
      println(s"max: $max")

    def calcOneToN(n: Int, acc: List[Int]): Vector[Int] =
      if      (n == 1  ) calcOneToN(n+1, List(1))
      else if (n <= max) calcOneToN(n+1, n+acc.head::acc)
      else 0::(acc reverse) toVector

    val oneToN = calcOneToN(1, Nil)
      println(s"oneToN: $oneToN")

    def fold(tl: List[Item], acc: Int): Int = {
//      println(s"$tl : $acc")
      tl match {
        case Nil           => acc
        case h::Nil        => fold(Nil,    acc + oneToN(h.cnt))
        case h::t::Nil     => fold(t::Nil, acc + oneToN(h.cnt))
        case l::c::r::tail =>
          if (l==r && c.cnt==1) fold(c::r::tail, acc + oneToN(l.cnt) + l.cnt)
          else                  fold(c::r::tail, acc + oneToN(l.cnt))
      }
    }

    val n = fold(ls.asInstanceOf[List[Item]], 0)
    println(n)
    n
  }

  val s = "abcbaba"
  substrCount(s.length, s)

}
