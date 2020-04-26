package hackerrankfp.d200425_04

/**
  * https://www.hackerrank.com/challenges/lambda-march-compute-the-perimeter-of-a-polygon/problem
  * 14/24 passed
  * TODO: this is convex implementation
  * TODO: need to be fixed for concave one
  * https://www.mathsisfun.com/geometry/area-irregular-polygons.html
  */
object PolygonAreaApp {
  import scala.math.sqrt
  import scala.math.abs
  def sq(x: Double): Double = x * x

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Pt(x: Double, y: Double) {
    def this(xy: Array[Int]) = this(xy(0), xy(1))
  }

  def distance(a: Pt, b: Pt): Double = sqrt(sq(a.x-b.x) + sq(a.y-b.y))

  def sign(p1: Pt, p2: Pt, p3: Pt): Double =
    (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)

  case class Triangle(a: Pt, b: Pt, c: Pt) {
    def abc3: (Double, Double, Double) = {
      val lab = distance(a, b)
      val lac = distance(a, c)
      val lbc = distance(b, c)
      (lab, lac, lbc)
    }
    def hc: Double = {
      val (lab, lac, lbc) = abc3
      val dc = (sq(lab) + sq(lbc) - sq(lac)) / (2 * lab)
      sqrt(abs(sq(lbc) - sq(dc)))
    }
    def area = hc * distance(a,b) / 2
    def area2 = {
      val (lab, lac, lbc) = abc3
      val s2 = (lab + lac + lbc)/2
      sqrt(s2*(s2-lab)*(s2-lac)*(s2-lbc))
    }
  }

  def listToTriangles(points: List[Pt]): List[Triangle] = {
    val p0 = points.head
    @scala.annotation.tailrec
    def make(acc: List[Triangle], pts: List[Pt]): List[Triangle] = pts match {
      case a::b::Nil  => Triangle(p0, a, b) :: acc
      case a::b::tail => make(Triangle(p0, a, b) :: acc, b::tail)
      case _          => ???
    }
    make(Nil, points.tail)
  }

  def calcArea(points: List[Pt]): Double = {
    listToTriangles(points)
      .foldLeft(0.toDouble) { (a, t) => a + t.area }
  }

  def process(points: List[Pt]): Double = calcArea(points)

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

  val fname = "src/main/scala/hackerrankfp/d200425_04/area.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
