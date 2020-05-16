package hackerrank.d200515_09

import scala.util.{Failure, Success, Try}
import scala.math.min

/**
  * https://www.hackerrank.com/challenges/special-palindrome-again/problem
  */
object SpecialStringAgainApp extends App {
  val oneToN = (n: Long) => n*(n+1)/2
  final case class Item(ch: Char, cnt: Long)

  def substrCount(N: Int, origin: String): Long = {

    def toTuples(idx: Int, acc: List[Item], buf: List[Char]): List[Item] = N-idx match {
      case 0 => Item(buf.head, buf.length)::acc
      case _ =>
        val c = origin(idx)
        buf match {
          case Nil   => toTuples(idx+1, acc, c::Nil)
          case bh::_ =>
            if (bh==c)  toTuples(idx+1, acc, c::buf)
            else        toTuples(idx+1, Item(bh, buf.length)::acc, c::Nil)
        }
    }

    def fold(tl: List[Item], acc: Long): Long = tl match {
      case Nil                     => acc
      case a::Nil                  => fold(Nil,        acc + oneToN(a.cnt))
      case a::b::Nil               => fold(b::Nil,     acc + oneToN(a.cnt))
      case a::b::c::tail
        if b.cnt==1 && a.ch==c.ch  => fold(b::c::tail, acc + oneToN(a.cnt) + min(a.cnt, c.cnt))
      case a::tail                 => fold(tail,       acc + oneToN(a.cnt))
    }

    val tuples = toTuples(0, Nil, Nil)
    fold(tuples, 0)
  }

  val fname = "src/main/scala/hackerrank/d200515_09/60K.txt"
  val s =
    scala.util.Using(
    scala.io.Source.fromFile(new java.io.File(fname))
  ) { src =>
    val it = src.getLines().map(_.trim)
    it.next()
  } match {
    case Failure(_) => ???
    case Success(s) => s
  }
  println(substrCount(s.length, s))
}
