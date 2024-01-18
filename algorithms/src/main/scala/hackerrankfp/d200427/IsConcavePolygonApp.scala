package hackerrankfp.d200427

/** https://www.hackerrank.com/challenges/lambda-march-concave-polygon/problem */
object IsConcavePolygonApp {
  import scala.math.atan2
  import scala.math.toDegrees
  def sq(x: Double): Double = x * x

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Pt(x: Double, y: Double) {
    def this(xy: Array[Int]) = this(xy(0), xy(1))
    def isAtRightFrom(line: Line): Boolean = line.isPtAtRight(this)
    def isAtLeftFrom(line: Line): Boolean = line.isPtAtLeft(this)
    def isOn(line: Line): Boolean = line.isPtOn(this)
  }

  /** line from a to b */
  case class Line(a: Pt, b: Pt) {

    /** a bit of math:
      * https://math.stackexchange.com/questions/274712/calculate-on-which-side-of-a-straight-line-is-a-given-point-located
      * sign = (x-x1)(y2-y1)-(y-y1)(x2-x1)
      */
    def sign(pt: Pt): Double = {
      val r = (pt.x - b.x) * (a.y - b.y) - (a.x - b.x) * (pt.y - b.y)
      if (r > 0) 1 else if (r < 0) -1 else 0
    }
    def isPtAtLeft(pt: Pt): Boolean = sign(pt) > 0
    def isPtAtRight(pt: Pt): Boolean = sign(pt) < 0
    def isPtOn(pt: Pt): Boolean = sign(pt) == 0
  }

  private def centroid(points: List[Pt]): Pt = {
    val cnt = points.length
    val sum = points.reduce { (p1, p2) => Pt(p1.x + p2.x, p1.y + p2.y) }
    Pt(sum.x / cnt, sum.y / cnt)
  }

  private def sort(pts: List[Pt]): List[Pt] = if (pts.length <= 3) pts
  else {
    def angle = (dx: Double, dy: Double) => (toDegrees(atan2(dx, dy)) + 360) % 360
    val c = centroid(pts)

    pts.sortWith { (a, b) =>
      val a1 = angle(a.x - c.x, a.y - c.y)
      val a2 = angle(b.x - c.x, b.y - c.y)
      a1 < a2
    }
  }

  def isConcave(ps: List[Pt]): Boolean = {
    val sign = Line(ps(0), ps(1)).sign(ps(2))

    def doCheck(px: List[Pt], acc: (Boolean, Double)): Boolean = px match {
      case Nil           => ???
      case _ :: _ :: Nil => true
      case a :: b :: c :: tail =>
        val sign = Line(a, b).sign(c)
        if (sign == acc._2) doCheck(b :: c :: tail, (true, sign)) else false
      case _ => ???
    }

    !doCheck(ps, (true, sign))
  }

  def process(psx: List[Pt]) = {
    val ps = sort(psx)
    if (isConcave(ps ::: (ps.head :: ps.tail.head :: Nil))) "YES" else "NO"
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    // String => Object
    def readPoint: Pt = new Pt(readLine.splitToInt)
    @scala.annotation.tailrec
    def addLines(n: Int, acc: List[Pt]): List[Pt] = n match {
      case 0 => acc.reverse
      case _ => addLines(n - 1, readPoint :: acc)
    }
    val points = addLines(N, Nil)
    val r = process(points)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200427_06/isconcave.txt"
  def main_file(p: Array[String]): Unit = {
    scala
      .util
      .Using(
        scala.io.Source.fromFile(new java.io.File(fname)),
      ) { src =>
        val it = src.getLines().map(_.trim)
        body { it.next() }
      }
  }

}
