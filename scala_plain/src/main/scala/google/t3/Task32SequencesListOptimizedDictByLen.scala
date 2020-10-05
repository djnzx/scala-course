package google.t3

import scala.collection.mutable

/**
  * longest sequence of the words from the given dictionary:
  *
  * dict = Set(
  * "i", "in", "ik", "sin", "sing", "sting", "string",
  * "like", "ike", "ide", "id", "king", "idea", "ikea",
  * "a", "ab", "abc", "ink", "kin", "king", "kind"
  * )
  *
  * you can construct from the given set:
  *
  * List(
  * List("a", "ab", "abc"),
  * List("i", "in", "ink"),
  * List("i", "in", "kin", "kind"),
  * List("i", "in", "kin", "king"),
  * List("i", "in", "sin", "sing", "sting", "string"),
  * List("i", "id", "ide", "idea"),
  * List("i", "ik", "ike", "ikea"),
  * List("i", "ik", "ike", "like"),
  * List("i", "ik", "ink")
  * )
  */
object Task32SequencesListOptimizedDictByLen extends App {

  import Task31Levenstein.distance

  val dict = Set(
    "i", "in", "ik", "sin", "sing", "sting", "string",
    "like", "ike", "ide", "id", "king", "idea", "ikea",
    "a", "ab", "abc", "ink", "kin", "king", "kind"
  )

  val dictByLen = mutable.Map.empty[Int, Set[String]]

  def nextWordsTo(word: String) =
    word.length + 1 match {
      case len =>
        dictByLen
          .getOrElseUpdate(len, dict.filter(_.length == len))
          .filter(distance(word, _) == 1)
          .toList
    }

  def allSeq(word: String = ""): List[List[String]] =
    nextWordsTo(word) match {
      case Nil => List(Nil)
      case ws => ws.flatMap(w => allSeq(w).map(w :: _))
    }

  def longestSeq() =
    allSeq()
      .map(_.length)
      .max

  println(allSeq())
}
