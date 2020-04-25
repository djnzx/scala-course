package hackerrankfp

object TemplateApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  def process(a: Int) = a

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    // one Point by readline
    readLine.toListInt
    def readPoint[A]: A = ???
    // N lines by readline
    def addLines[A](n: Int, acc: List[A]): List[A] = n match {
      case 0 => acc.reverse
      case _ => addLines(n-1, readPoint::acc)
    }

    val r = process(1)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200425_04/convexFailed.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
