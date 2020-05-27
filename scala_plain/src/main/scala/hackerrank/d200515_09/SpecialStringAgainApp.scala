package hackerrank.d200515_09

import scala.math.min
import scala.util.{Failure, Success}

/**
  * htt`ps://www.hackerrank.com/challenges/special-palindrome-again/problem
  */
object SpecialStringAgainApp extends App {

  def substrCount(N: Int, origin: String): Long = {
    val oneToN = (n: Long) => n*(n+1)/2
    final case class Item(ch: Char, cnt: Long) {
      def inc = Some(Item(ch, cnt + 1))
    }
    object Item {
      val one = (c: Char) => Some(Item(c,1))
    }

    def toTuples(idx: Int, acc: List[Item], buf: Option[Item]): List[Item] =
      if (idx==N) buf.get::acc
      else {
        val c = origin(idx)
        buf match {
          case None     => toTuples(idx+1, acc,          Item.one(c))
          case Some(bh)
            if bh.ch==c => toTuples(idx+1, acc,          bh.inc)
          case _        => toTuples(idx+1, buf.get::acc, Item.one(c))
        }
      }

    def fold(tl: List[Item], acc: Long): Long = tl match {
      case Nil     => acc
      case a::tail =>
        val acc2 = acc + oneToN(a.cnt)
        tail match {
          case b::c::_ if b.cnt==1 && a.ch==c.ch
                   => fold(tail, acc2 + min(a.cnt, c.cnt))
          case _   => fold(tail, acc2)
        }
    }

    val tuples = toTuples(0, Nil, None)
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
