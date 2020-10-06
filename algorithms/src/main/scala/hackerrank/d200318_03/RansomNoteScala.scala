package hackerrank.d200318_03

import scala.collection.mutable

/**
  * https://www.hackerrank.com/challenges/ctci-ransom-note/problem
  */
object RansomNoteScala extends App {

  def checkMagazine(magazine: Array[String], note: Array[String]) {
    val words = magazine
      .groupBy(identity)
      .map { case (w, a) => (w, a.length) }
      .to(mutable.Map)

    val ok = note.forall { w => words.updateWith(w) {
      case Some(x) => Some(x - 1)
      case None => Some(-1)
    }.get >=0 }

    println(if (ok) "Yes" else "No")
  }

}
