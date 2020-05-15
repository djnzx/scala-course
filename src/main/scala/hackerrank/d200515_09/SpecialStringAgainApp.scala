package hackerrank.d200515_09

import scala.collection.mutable

/**
  * https://www.hackerrank.com/challenges/special-palindrome-again/problem
  * doesn't pass the tests because of timeout
  */
object SpecialStringAgainApp extends App {

//  def fabc[A, B, C](a: A, b: B): C = ???
//  def fabcc1[A, B, C](a: A): B => C = (b: B) => fabc(a, b)
//  def fabcc2[A, B, C](b: B): A => C = (a: A) => fabc(a, b)
//  def fabct[A, B, C](t: (A, B)): C = fabc(t._1, t._2)

  def allTheSameEven(s: String): Boolean = {
    var idx = 1
    val c = s(0)
    while (idx < s.length) {
      if (s(idx) != c) return false
      idx +=1
    }
    true
  }
  def allTheSameOdd(s: String): Boolean = {
    var idx = 1
    val c = s(0)
    while (idx < s.length) {
      if (idx != s.length/2 && s(idx) != c) return false
      idx +=1
    }
    true
  }

  def isSpecial(s: String): Boolean = {
    val l = s.length
    if (l == 1) true else
      if (l % 2 == 0) allTheSameEven(s) else
        allTheSameOdd(s)
  }

  val special: mutable.Map[String, Long] = mutable.Map[String, Long]()
  val wrong: mutable.Map[String, Long] = mutable.Map[String, Long]()

  def substrCount(n: Int, s: String): Long = {
    (1 to n).flatMap { len =>
      (0 to n - len).map { start =>
        s.substring(start, start + len)
      }
    }
      .foreach { s =>
        if      (special.contains(s)) special.updateWith(s) { _.map { _+1 } }
        else if (wrong.contains(s)) { }
        else if (isSpecial(s)) special.put(s, 1)
        else    wrong.put(s, 1)
      }

    special.toVector map {_._2} sum
  }

  println(substrCount(4, "aaaa"))

}
