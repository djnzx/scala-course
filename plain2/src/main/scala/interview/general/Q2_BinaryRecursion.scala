package interview.general

object Q2_BinaryRecursion extends App {

  val a = List("0", "1")

  def combinations(len: Int): List[String] = {

    def go(n: Int, acc: List[String]): List[String] = n match {
      case 0 => acc
      case n => go(n - 1, a.flatMap(x => acc.map(x + _)))
    }

    go(len, List(""))
  }

  pprint.pprintln(combinations(4))

}
