package hackerrankfp.d200601_07

/**
  * https://www.hackerrank.com/challenges/sequence-full-of-colors/problem
  */
object SequenceFullOfColors {
  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class CC(r: Int, g: Int, y: Int, b: Int) {
    def process(c: Char): Option[CC] = {
      val c2 = c match {
        case 'R' => this.copy(r = r + 1)
        case 'G' => this.copy(g = g + 1)
        case 'Y' => this.copy(y = y + 1)
        case 'B' => this.copy(b = b + 1)
      }
      if ((math.abs(c2.r - c2.g)<=1) && (math.abs(c2.y - c2.b)<=1)) Some(c2) 
      else None
    }
  }
  def represent(value: Boolean): String = if (value) "True" else "False"
  
  def processOne(s: String): Boolean = {
    def processOneR(i: Int, cco: Option[CC]): Option[CC] = cco match {
      case None => None
      case Some(cc) => if (i < s.length) processOneR(i+1, cc.process(s(i))) else cco 
    }
    processOneR(0, Some(CC(0, 0, 0, 0))) match {
      case None => false
      case Some(CC(r, g, y, b)) => r == g && y == b 
    }
  }
  
  /** core implementation */
  def process(data: List[String]) = {
    data map processOne map represent
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    process(list).foreach(println)
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
