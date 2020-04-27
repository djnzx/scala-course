package hackerrankfp.d200427_06

/**
  * https://www.hackerrank.com/challenges/lambda-march-concave-polygon/problem
  */
object IsConcavePolygonApp {
  import scala.math.sqrt
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
    /**
      * a bit of math:
      * https://math.stackexchange.com/questions/274712/calculate-on-which-side-of-a-straight-line-is-a-given-point-located
      * sign = (x-x1)(y2-y1)-(y-y1)(x2-x1)
      */
    def sign(pt: Pt): Double = {
      val r = (pt.x - b.x) * (a.y - b.y) - (a.x - b.x) * (pt.y - b.y)
      if (r>0) 1 else if (r<0) -1 else 0
    }
    def isPtAtLeft(pt: Pt): Boolean = sign(pt) > 0
    def isPtAtRight(pt: Pt): Boolean = sign(pt) < 0
    def isPtOn(pt: Pt): Boolean = sign(pt) == 0
  }

  def distance(a: Pt, b: Pt): Double = sqrt(sq(a.x-b.x) + sq(a.y-b.y))

  case class Triangle(a: Pt, b: Pt, c: Pt) {
    def sides: (Double, Double, Double) = {
      val lab = distance(a, b)
      val lac = distance(a, c)
      val lbc = distance(b, c)
      (lab, lac, lbc)
    }
    def isInside(p: Pt): Boolean = {
      val d1 = Line(a,b).sign(p)
      val d2 = Line(b,c).sign(p)
      val d3 = Line(c,a).sign(p)
      val neg = d1<0 && d2<0 && d3<0
      val pos = d1>0 && d2>0 && d3>0
      neg ^ pos
    }
    def isNoneInside(pts: List[Pt]): Boolean = pts.forall(!isInside(_))
    def isAnyInside(pts: List[Pt]): Boolean = pts.exists(isInside)
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

  @scala.annotation.tailrec
  def listToTrianglesConcave(pts: List[Pt], trios: List[Triangle]): List[Triangle] = pts match {
    case a :: b :: c :: Nil => Triangle(a,b,c) :: trios
    case a :: b :: c :: tail => {
      val t = Triangle(a,b,c)
      val ac = Line(a, c)
      if (ac.isPtAtRight(b) && t.isNoneInside(pts))
           listToTrianglesConcave(a::c::tail,         t::trios)
      else listToTrianglesConcave(b::c::tail:::(a::Nil), trios)
    }
    case _ => ???
  }

  def isConcave(ps: List[Pt]): Boolean = {
    val sign = Line(ps(0), ps(1)).sign(ps(2))

    def doCheck(px: List[Pt], acc: (Boolean, Double)): Boolean = px match {
      case a::b::Nil => true
      case a::b::c::tail => {
        val sign = Line(a,b).sign(c)
        if (sign == acc._2) doCheck(b::c::tail, (true, sign)) else false
      }
    }

    !doCheck(ps, (true, sign))
  }

  def process(ps: List[Pt]) =
    if (isConcave(ps ::: (ps.head :: ps.tail.head :: Nil))) "YES" else "NO"

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

  val fname = "src/main/scala/hackerrankfp/d200427_06/isconcave.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
