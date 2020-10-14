package google.live.t3

import pprint.{pprintln => println}
import scala.collection.mutable

/**
  * since we don't actually need the sequence,
  * but we need the length,
  * we can 
  */
object Task33MaxLenOnly extends App {

  import Task30Levenstein.distance
  import Task3Data._

  val dictByLen = mutable.Map.empty[Int, Set[String]]

  def nextWordsTo(word: String) =
    word.length + 1 match {
      case len =>
        dictByLen
          .getOrElseUpdate(len, dict.filter(_.length == len))
          .filter(distance(word, _) == 1)
          .toList
    }

  def maxLen(word: String = ""): Int =
    nextWordsTo(word) match {
      case Nil => 0
      case ws  => 1 + ws.flatMap(w => List(maxLen(w))).max
    }

  println(maxLen())
}
