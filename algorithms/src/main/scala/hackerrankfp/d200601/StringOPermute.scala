package hackerrankfp.d200601

/**
  * https://www.hackerrank.com/challenges/string-o-permute/problem
  */
object StringOPermute {
  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  /** core implementation */
  def processOne(s: String): String = {
    
    def proc(i: Int, max: Int, buf: List[Char]): List[Char] =
      if (i < max) proc(i+1, max, s(i*2)::s(i*2+1)::buf)
      else buf

    proc(0, s.length/2, Nil).reverse.mkString
  }

  def process(data: List[String]): List[String] =
    data map processOne

  def body(line: => String): Unit = {
    // read one line
    val N = line.toInt
    // read N lines
    val list = (1 to N).map { _ => line }.toList
    process(list)
      .foreach(println)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("1.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala.util.Using(
      scala.io.Source.fromFile(file)
    ) { src =>
      val it = src.getLines().map(_.trim)
      process(it.next())
    }.fold(_ => ???, identity)
  }

}
