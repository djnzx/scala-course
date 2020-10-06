package hackerrank.d200515_09

/**
  * https://www.hackerrank.com/challenges/ctci-making-anagrams/problem
  */
object MakingAnagramsApp extends App {
  def makeAnagram(a: String, b: String): Int = {
    val f = (s: String, g: Iterable[_] => Int) => s.view groupBy { identity } map { case (c, l) => (c, g(l)) } toVector
    val ma = f(a, _.size)
    val mb = f(b, - _.size)
    val sum = ma ++ mb groupBy { case (c, _) => c } map { case (c, v) => (c, v map { _._2 } sum) } toVector

    sum map { case(_,c) => math.abs(c) } sum
  }
  println(makeAnagram("abcdex","abcdef"))
}
