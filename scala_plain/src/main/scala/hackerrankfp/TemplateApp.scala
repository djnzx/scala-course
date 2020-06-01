package hackerrankfp

object TemplateApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  /** core implementation */
  def process(data: List[String]) = {
    println(data)
  } 
  
  def body(line: => String): Unit = {
    // read one line
    val N = line.toInt
    // read N lines
    val list = (1 to N).map { _ => line }.toList
    process(list)
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
