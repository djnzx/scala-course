package hackerrankfp.d200425_04

/**
  * https://www.hackerrank.com/challenges/lambda-march-compute-the-area-of-a-polygon/problem
  * 14/24 passed
  * - convex implementation
  * - star implementation
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

  case class Triangle(a: Pt, b: Pt, c: Pt) {
    def sides: (Double, Double, Double) = {
      val lab = distance(a, b)
      val lac = distance(a, c)
      val lbc = distance(b, c)
      (lab, lac, lbc)
    }
    def area = {
      val (lab, lac, lbc) = sides
      val s2 = (lab + lac + lbc)/2
      sqrt(s2*(s2-lab)*(s2-lac)*(s2-lbc))
    }
  }

  def listToTrianglesConvex(points: List[Pt]): List[Triangle] = {
    val p0 = points.head
    @scala.annotation.tailrec
    def make(acc: List[Triangle], pts: List[Pt]): List[Triangle] = pts match {
      case a::b::Nil  => Triangle(p0, a, b) :: acc
      case a::b::tail => make(Triangle(p0, a, b) :: acc, b::tail)
      case _          => ???
    }
    make(Nil, points.tail)
  }

  private def centroid(points: List[Pt]): Pt = {
    val cnt = points.length
    val sum = points.reduce { (p1, p2) => Pt(p1.x+p2.x, p1.y+p2.y) }
    Pt(sum.x/cnt, sum.y/cnt)
  }

  def listToTrianglesStar(points: List[Pt]): List[Triangle] = {
    val p0 = centroid(points)
    @scala.annotation.tailrec
    def make(acc: List[Triangle], pts: List[Pt]): List[Triangle] = pts match {
      case Nil        => acc
      case z::Nil     => make(Triangle(p0, z, points.head) :: acc, Nil)
      case a::b::tail => make(Triangle(p0, a, b          ) :: acc, b::tail)
    }
    make(Nil, points)
  }

  def calcArea(points: List[Pt]): Double =
    listToTrianglesStar(points)
      .foldLeft(0.toDouble) { (a, t) => a + t.area }

  def process(ps: List[Pt]) =
    calcArea(ps)

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
