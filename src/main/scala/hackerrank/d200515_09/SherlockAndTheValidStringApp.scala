package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
  */
object SherlockAndTheValidStringApp extends App {
  val YES = "YES"
  val NO = "NO"

  def isValid(s: String): String = {
    val m = s.toVector
      .groupBy { identity }
      .map { case (c, l) => (c, l.size) }
      .toVector
      .groupBy { case (_, cnt) => cnt }
      .toVector
      .map { case (cnt, v) => (cnt, v map { _._1 }) }
      .sortBy { _._1 }

    if (m.length == 1) YES
    else if (m.length > 2) NO
    else if (m(1)._1 - m(0)._1 == 1 && m(1)._2.length == 1) YES
    else if (m(0)._1==1 && m(0)._2.length==1) YES
    else NO
  }

}
