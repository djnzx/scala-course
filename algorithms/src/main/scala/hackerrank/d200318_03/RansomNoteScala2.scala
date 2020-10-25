package hackerrank.d200318_03

/**
  * https://www.hackerrank.com/challenges/ctci-ransom-note/problem
  */
object RansomNoteScala2 extends App {

  def checkMagazine(magazine: Array[String], note: Array[String]) = {
    def toMap(a: Array[String]) = a.groupMapReduce(identity)(_=>1)(_+_) 
    val have = toMap(magazine)
    val need = toMap(note)
    val ok = need.forall { case (w, c) => have.getOrElse(w, 0) >= c }

    println(if (ok) "Yes" else "No")
  }

}
