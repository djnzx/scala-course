package hackerrankfp.d200612_10

object SimplifyAlgebraicExpressions {
  
  /** core implementation */
  def process(data: List[String]) = {
    data
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    val r = process(list)
    r.foreach { println }
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("parser1.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala.util.Using(
      scala.io.Source.fromFile(file)
    ) { src =>
      val it = src.getLines().map(_.trim)
      try { process(it.next()) }
      catch { case x: Throwable => x.printStackTrace() }
    }.fold(_ => ???, identity)
  }

}
