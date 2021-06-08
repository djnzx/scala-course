package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
  *
  * O(N + D * Log D)
  * D << N
  */
object SherlockAndTheValidStringApp extends App {
  val YES = "YES"
  val NO = "NO"

  def isValid(s: String): String =
    s.toVector
      .groupBy { identity }
      .map { case (c, l) => (c, l.size) }
      .toVector
      .groupBy { case (_, cnt) => cnt }
      .toVector
      .map { case (cnt, v) => (cnt, v map { _._1 }) }
      .map { case (a, b) => (a, b.length) }
      .sortBy { _._1 }
    match {
      case m if m.length == 1                          => YES  // all have the same frequency
      case m if m.length > 2                           => NO   // more than 2 different frequencies => can't be fixed
      case m if m(1)._1 - m(0)._1 == 1 && m(1)._2 == 1 => YES  // bigger freq - smaller freq = 1 && bigger.length = 1
      case m if m(0)._1==1             && m(0)._2 == 1 => YES  // smaller len = 1 && smaller freq = 1
      case _                                           => NO
    }

}
