package hackerrankfp.d200602_08.prison

/** https://www.hackerrank.com/challenges/prison-transport/problem doesn't pass one test
  */
object PrisonTransportV1 {

  import scala.collection.mutable

  def priceOneGroup(n: Int): Int = math.ceil(math.sqrt(n)).toInt

  def priceAllGroups(xs: Iterable[Int]): Int = xs.map { priceOneGroup }.sum

  def priceTotal(N: Int, xs: Iterable[Int]): Int = N - xs.sum + priceAllGroups(xs)

  def processPair(a: Int, b: Int, m: mutable.Map[Int, Int]): mutable.Map[Int, Int] = {
    (m.contains(a), m.contains(b)) match {
      case (false, false) =>
        m.addOne(a -> a)
        m.addOne(b -> a)

      case (true, false) =>
        m.addOne(b -> m(a))

      case (false, true) =>
        m.addOne(a -> m(b))

      case (true, true) =>
        val g1 = m(a)
        val g2 = m(b)
        val patch = m.filter { case (_, v) => v == g1 }.keys.map { _ -> g2 }
        m.addAll(patch)
    }
    m
  }

  def simplify(m: mutable.Map[Int, Int]): Iterable[Int] = m
    .groupBy { case (_, v) => v } // group by values
    .map { case (_, m) => m.size } // use only sizes of the group

  def process(N: Int, pairs: Seq[(Int, Int)]): Int = {
    val map: mutable.Map[Int, Int] = pairs.foldLeft(mutable.Map.empty[Int, Int]) { case (m, (a, b)) =>
      processPair(a, b, m)
    }
    val groups = simplify(map)
    val t = priceTotal(N, groups)
    t
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val nPairs = line.toInt
    val pairs = (1 to nPairs).map { _ => line }.map {
      _.splitToInt match {
        case Array(a, b) => (a, b)
        case _           => ???
      }
    }
    val r = process(N, pairs)
    println(r)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("prison.txt", body)
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
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }
}
