package hackerrankfp.d200421_01

object IntroApp extends App {
  /**
    * https://www.hackerrank.com/challenges/fp-filter-positions-in-a-list/problem
    */
  def f01(arr: List[Int]): List[Int] =
    arr.zipWithIndex
    .filter { _._2 % 2 != 0 }
    .map { _._1 }

  /**
    * https://www.hackerrank.com/challenges/fp-array-of-n-elements/problem
    */
  def f02a(num:Int): List[Int] =
    Array.fill[Int](num)(1).toList
  def f02b(num:Int): List[Int] =
    1 to num toList

  def readInt = scala.io.Source.stdin.getLines().take(1).map(_.toInt).sum
  println(f02a(readInt))

  /**
    * https://www.hackerrank.com/challenges/fp-reverse-a-list/problem?h_r=next-challenge&h_v=zen
    */
  def f03(arr:List[Int]):List[Int] = {
    def fr(xs: List[Int], acc: List[Int]): List[Int] = xs match {
      case h::t => fr(t, h::acc)
      case _ => acc
    }
    fr(arr, List.empty[Int])
  }

  /**
    * https://www.hackerrank.com/challenges/fp-sum-of-odd-elements/problem?h_r=next-challenge&h_v=zen
    */
  def f(arr:List[Int]):Int = arr.foldLeft(0) { (acc, el) => acc + (if (el %2 != 0) el else 0)}
}
