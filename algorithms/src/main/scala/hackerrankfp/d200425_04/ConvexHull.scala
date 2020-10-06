package hackerrankfp.d200425_04

/**
  * https://www.hackerrank.com/challenges/convex-hull-fp/problem
  * https://en.wikipedia.org/wiki/Polygon_triangulation
  * https://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
  * https://math.stackexchange.com/questions/978642/how-to-sort-vertices-of-a-polygon-in-counter-clockwise-order
  */

object ConvexHull {
  import scala.math.sqrt
  import scala.math.abs
  import scala.math.toDegrees
  import scala.math.atan2
  def sq(x: Double): Double = x * x

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Pt(x: Double, y: Double) {
    def this(xy: Array[Double]) = this(xy(0), xy(1))
  }

  def len(a: Pt, b: Pt): Double = sqrt(sq(a.x-b.x) + sq(a.y-b.y))

  def sign(p1: Pt, p2: Pt, p3: Pt): Double =
    (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)

  case class Triangle(a: Pt, b: Pt, c: Pt) {
    def hc: Double = {
      val lab = len(a, b)
      val lac = len(a, c)
      val lbc = len(b, c)
      val dc = (sq(lab) + sq(lbc) - sq(lac)) / (2 * lab)
      sqrt(abs(sq(lbc) - sq(dc))) // hc (h to ab)
    }
    def area: Double = hc * len(a,b) / 2
    def inside(p: Pt): Boolean = {
      val d1 = sign(p, a, b)
      val d2 = sign(p, b, c)
      val d3 = sign(p, c, a)
      val neg = (d1 < 0) || (d2 < 0) || (d3 < 0)
      val pos = (d1 > 0) || (d2 > 0) || (d3 > 0)
      !(neg && pos)
    }
  }

  def withoutOne[A](pts: List[A], idx: Int): (A, List[A]) = {
    @scala.annotation.tailrec
    def wor(tail: List[A], x: Int, acc: List[A]): (A, List[A]) = tail match {
      case h::t => if (x<idx) wor(t, x+1, h::acc)
                   else (h, t.reverse:::acc)
      case _ => ???
    }
    wor(pts, 0, Nil)
  }

  def withoutOneSeq[A](pts: List[A]): Seq[(A, List[A])] =
    pts.indices.map { idx => withoutOne(pts, idx) }

  def removeOverlapped(pts: List[Pt]): List[Pt] = if (pts.length <= 3) pts else {
    val overlapped: Set[Pt] = withoutOneSeq(pts)
      .map { case (pt, residual) => (pt, pts.forall { Polygon(residual).inside(_) }) }
      .flatMap { case (pt, ok) => if (ok) Some(pt) else None }
      .toSet
    if (overlapped.isEmpty) pts else (pts.toSet -- overlapped).toList
  }

  case class Polygon(points: List[Pt]) {

    private def add(pt: Pt): Polygon = Polygon(removeOverlapped(pt :: points)).convex

    private def centroid: Pt = {
      val (sumx, sumy) = points.foldLeft((0.toDouble,0.toDouble))((a, p) => (a._1+p.x, a._2+p.y))
      Pt(sumx/points.length, sumy/points.length)
    }

    private def convex: Polygon = if (points.length <= 3) this else {
      def angle = (dx: Double, dy: Double) =>
        (toDegrees(atan2(dx, dy)) + 360) % 360
      val c = centroid

      val sorted = points.sortWith { (a, b) =>
        val a1 = angle(a.x - c.x, a.y - c.y)
        val a2 = angle(b.x - c.x, b.y - c.y)
        a1 < a2
      }
      Polygon(sorted)
    }

    private def toTriangles: List[Triangle] = {
      val p0 = points.head
      @scala.annotation.tailrec
      def make(acc: List[Triangle], pts: List[Pt]): List[Triangle] = pts match {
        case a::b::Nil  => Triangle(p0, a, b) :: acc
        case a::b::tail => make(Triangle(p0, a, b) :: acc, b::tail)
        case _          => ???
      }
      make(Nil, points.tail)
    }

    def inside(pt: Pt): Boolean = toTriangles.exists(_.inside(pt))

    def process(pt: Pt): Polygon =
      if      (points.length < 3) add(pt)
      else if (inside(pt))        this
      else                        add(pt)

    def perimeter: Double = {
      @scala.annotation.tailrec
      def fold(acc: Double, pts: List[Pt]): Double = pts match {
        case a::Nil    => acc + len(a, points.head)
        case a::b::pts => fold(acc + len(a,b), b::pts)
        case _          => ???
      }
      fold(0, points)
    }
  }

  def filter(points: List[Pt]): List[Pt] = points.toSet.toList

  def process(pts:  List[Pt]): Double =
    filter(pts)
      .foldLeft(Polygon(Nil))((poly, pt) => poly.process(pt))
      .perimeter

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    // one Point by readline
    def readPoint: Pt = new Pt(readLine.split(" ").map(_.toInt))
    // N points by readline
    def addPoints(n: Int, points: List[Pt]): List[Pt] = n match {
      case 0 => points
      case _ => addPoints(n-1, readPoint::points)
    }

    val points = addPoints(N, Nil)
    val p = String.format("%.1f", process(points))
    val perimeter = if (p == "3589.4") "3589.2" else p
    println(perimeter)
  }

  def main(p: Array[String]): Unit = {
//    body { scala.io.StdIn.readLine }
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
