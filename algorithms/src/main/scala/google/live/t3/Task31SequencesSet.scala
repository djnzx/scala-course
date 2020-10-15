package google.live.t3

/**
  * longest sequence of
  */
object Task31SequencesSet extends App {

  import LevensteinDistance.distance
  import Task3Data._

  def nextWordsTo(word: String) =
    dict
      .filter(w => (w.length == word.length + 1) && distance(word, w) == 1)

  def allSeq(word: String = ""): Set[List[String]] =
    nextWordsTo(word) match {
      case ws if ws.isEmpty => Set(Nil)
      case ws => ws.flatMap(w => allSeq(w).map(w :: _))
    }

  def longestSeq() =
    allSeq()
      .map(_.length)
      .max

  println(allSeq())
}
