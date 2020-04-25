package hackerrankfp.d200425_04

/**
  * https://www.hackerrank.com/challenges/lambda-march-compute-the-perimeter-of-a-polygon/problem
  */
object PolygonPerimeterApp {
  import scala.math.sqrt
  def sq(x: Double): Double = x * x

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Pt(x: Int, y: Int) {
    def this(xy: Array[Int]) = this(xy(0), xy(1))
  }

  def distance(a: Pt, b: Pt): Double = sqrt(sq(a.x-b.x) + sq(a.y-b.y))

  def perimeter(points: List[Pt]): Double = {
    @scala.annotation.tailrec
    def calc(tail: List[Pt], acc: Double): Double = tail match {
      case Nil     => acc
      case a::Nil  => acc + distance(a,points.head)
      case a::b::t => calc(b::t, acc + distance(a,b))
    }
    calc(points, 0)
  }

  def process(points: List[Pt]): Double = perimeter(points)

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    // String => Object
    def readPoint: Pt = new Pt(readLine.splitToInt)
    @scala.annotation.tailrec
    def addLines(n: Int, acc: List[Pt]): List[Pt] = n match {
      case 0 => acc.reverse
      case _ => addLines(n-1, readPoint::acc)
    }
    val points = addLines(N, Nil)

    val r = process(points)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200425_04/polygon.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
