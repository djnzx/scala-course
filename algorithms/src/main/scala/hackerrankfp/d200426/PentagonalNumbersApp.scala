package hackerrankfp.d200426

object PentagonalNumbersApp {
  def pentaN(n: Long): Long = (n-1) * 5
  def commonPoints(n: Long): Long = (n-2) * 2 +1

  def allPentaUpTo(max: Int): Array[Long] = {
    val pentas = Array.fill[Long](max)(1)
    (2 to max).foreach { n =>
      val idx = n-1
      pentas(idx) = pentaN(n) + pentas(idx-1) - commonPoints(n)
    }
    pentas
  }

  def process(data: List[Int]) = {
    val pentas = allPentaUpTo(data.max)
    data map { n => pentas(n-1) } mkString "\n"
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readOneLine = readLine.toInt
    // N lines by readline
    def readNLines(n: Int, acc: List[Int]): List[Int] = n match {
      case 0 => acc.reverse
      case _ => readNLines(n-1, readOneLine::acc)
    }

    val r = process(readNLines(N, Nil))
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/penta.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
