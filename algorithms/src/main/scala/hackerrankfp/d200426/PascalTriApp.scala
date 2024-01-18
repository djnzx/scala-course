package hackerrankfp.d200426

/**
  * https://www.hackerrank.com/challenges/pascals-triangle/problem
  */
object PascalTriApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }
  def fact(n: Int): Long = (1 to n).foldLeft(1: Long)  ((acc, el) => acc * el)

  def pascalLine(n_row: Int): String = {
    @scala.annotation.tailrec
    def pascalLineR(n_col: Int, acc: List[Long]): List[Long] = {
      def item: Long = fact(n_row) / (fact(n_col) * fact(n_row-n_col))
      if (n_col > n_row) acc.reverse
      else pascalLineR(n_col+1, item::acc)
    }
    pascalLineR(0, Nil).mkString(" ")
  }

  def pascalTriangle(rows: Int): String = {
    @scala.annotation.tailrec
    def pascalTriangleR(n_row: Int, acc: List[String]): List[String] =
      if (n_row == rows) acc.reverse
      else pascalTriangleR(n_row + 1, pascalLine(n_row) :: acc)

    pascalTriangleR(0, Nil).mkString("\n")
  }

  def process(rows: Int): String =
    pascalTriangle(rows)

  def body(readLine: => String): Unit = {
    val n: Int = readLine.toInt
    val r = process(n)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/pascal.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
