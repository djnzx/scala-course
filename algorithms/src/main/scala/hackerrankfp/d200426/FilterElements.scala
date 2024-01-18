package hackerrankfp.d200426

object FilterElements {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
  }

  type TI = (Int, Int)
  def process1(tc: TestCase) = {
    tc.data.groupBy(identity)
      .filter(t => t._2.length >= tc.n)
      .keys.toArray
      .map { x => (x, tc.data.indexOf(x)) }
      .sorted { (x: TI, y: TI) => x._2 - y._2 }
      .map { _._1}
  }

  def process(data: List[TestCase]) =
    data
      .map { process1 }
      .map { l => if (l.nonEmpty) l.mkString(" ") else "-1"  }
      .mkString("\n")

  case class TestCase(n: Int, data: Array[Int])

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readOneTestCase = TestCase (
      readLine.splitToInt(1),
      readLine.splitToInt
    )
    // N lines by readline
    def readNTestCases(n: Int, acc: List[TestCase]): List[TestCase] = n match {
      case 0 => acc.reverse
      case _ => readNTestCases(n-1, readOneTestCase::acc)
    }

    val r = process(readNTestCases(N, Nil))
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/filterel.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
