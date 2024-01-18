package hackerrank.d200515

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
      .groupBy(identity)
      .map { case (c, cc) => (c, cc.size) }
      .groupBy { case (_, cnt) => cnt }
      .map { case (cnt, v) => (cnt, v.size) }
      .toVector
      .sortBy(_._1)
    match {
      case Vector(_)                              => YES // all have the same frequency
      case Vector((f0, _), (f1, 1)) if f1-f0 == 1 => YES // bigger freq - smaller freq = 1 && bigger.length = 1
      case Vector((1, 1), _)                      => YES // smaller len = 1 && smaller freq = 1
      case _                                      => NO
    }

}
