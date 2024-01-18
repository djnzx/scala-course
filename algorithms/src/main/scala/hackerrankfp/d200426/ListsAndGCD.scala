package hackerrankfp.d200426

/** https://www.hackerrank.com/challenges/lists-and-gcd/problem */
object ListsAndGCD {
  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  @scala.annotation.tailrec
  def toPairs(x: List[Int], acc: List[(Int, Int)]): List[(Int, Int)] = x match {
    case Nil            => acc.reverse
    case a :: b :: tail => toPairs(tail, (a, b) :: acc)
    case _              => ???

  }

  def process(data: List[List[Int]]) = {
    val maps = data.map { toPairs(_, Nil) } map { _.toMap }
    maps
      .foldLeft(Set.empty[Int]) { (acc, item) => acc ++ item.keys }
      .map { p => (p, maps.map { _.getOrElse(p, 0) }.min) }
      .filter { _._2 > 0 }
      .toList
      .sorted
      .map { t => s"${t._1} ${t._2}" }
      .mkString(" ")
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readOneLine = readLine.toListInt
    // N lines by readline
    def readNLines(n: Int, acc: List[List[Int]]): List[List[Int]] = n match {
      case 0 => acc.reverse
      case _ => readNLines(n - 1, readOneLine :: acc)
    }

    val r = process(readNLines(N, Nil))
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/listgcd.txt"
  def main_file(p: Array[String]): Unit = {
    scala
      .util
      .Using(
        scala.io.Source.fromFile(new java.io.File(fname)),
      ) { src =>
        val it = src.getLines().map(_.trim)
        body { it.next() }
      }
  }

}
