package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
  *
  * O(N + D * Log D)
  * D << N
  */
object SherlockAndTheValidStringApp2 extends App {
  val YES = "YES"
  val NO = "NO"

  def isValid(st: String) =
    st.groupMapReduce(identity)(_ => 1)(_ + _)
      .groupMapReduce(_._2)(_ => 1)(_ + _)
      .toVector
      .sortBy(_._1)
    match {
      case Vector(_)                              => YES // all have the same frequency
      case Vector((f0, _), (f1, 1)) if f1-f0 == 1 => YES // bigger freq - smaller freq = 1 && bigger.length = 1
      case Vector((_, 1), _)                      => YES // smaller freq = 1
      case _                                      => NO
    }

}
