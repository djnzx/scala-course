package google

import pprint.{pprintln => println}

/**
  * longest sequence of 
  */
object Task32Sequences extends App {
  
  import Task31Levenstein.distance

  val dict = Set(
    "i", "in", "ik", "sin", "sing", "sting", "string",
    "like", "ike", "ide", "id", "king", "idea", "ikea",
    "a", "ab", "abc", "ink", "kin", "king", "kind"
  )
  
  def nextWordsTo(word: String) =
    dict
      .filter(w => (w.length == word.length + 1) && distance(word, w) == 1)

  def allSeq(word: String = ""): Set[List[String]] =
    nextWordsTo(word) match {
      case ws if ws.isEmpty => Set(Nil)
      case ws => ws.flatMap(w => allSeq(w).map(w::_))
    }

  def longestSeq() =
    allSeq()
      .map(_.length)
      .max
      
  println(allSeq())
}
