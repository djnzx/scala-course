package hackerrankfp.d200426.subsetsum

/** https://www.hackerrank.com/challenges/subset-sum/problem */
object SubsetSumV3 {
  import scala.collection.Searching

  def calcSubSums(xs: Seq[Int]) = xs
    .foldLeft(List(0L)) {
      case (rh :: rt, x) => (rh + x) :: rh :: rt
      case _             => ???
    }
    .reverse
    .toArray

  def findSize(xs: Array[Long], value: Long) = xs.search(value) match {
    case Searching.Found(index)       => index
    case Searching.InsertionPoint(ip) => if (ip >= xs.length) -1 else ip
  }

  def process(xs: Array[Int], test: Seq[Long]) = {
    val xss: Array[Int] = xs.sorted(Ordering.Int.reverse)
    val subSums: Array[Long] = calcSubSums(xss)

    test.map { findSize(subSums, _) }
  }

  def body(line: => String): Unit = {
    val _ = line.toInt
    val data = line.splitToIntArray
    val N = line.toInt
    val test = (1 to N).map { _ => line }.map { _.toLong }
    process(data, test).foreach(println)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("subset100K.txt", body)

  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala
      .util
      .Using(
        scala.io.Source.fromFile(file),
      ) { src =>
        val it = src.getLines().map(_.trim)
        process(it.next())
      }
      .fold(_ => ???, identity)
  }
  implicit class StringToOps(s: String) {
    def splitToIntArray: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToIntArray.toVector
    def toListInt: List[Int] = splitToIntArray.toList
    def toTuple2Int: (Int, Int) = { val a = splitToIntArray; (a(0), a(1)) }
  }
}
