package hackerrankfp.d230407

// https://www.hackerrank.com/challenges/functions-and-fractals-sierpinski-triangles
// https://www.hackerrank.com/leaderboard?filter=Ukraine&filter_on=country&page=1&track=fp&type=practice
object SierpinskiTriangles {

  type Triangle = Array[String]

  val EMPTY  = "_"
  val FILLED = "1"

  def base(n: Int): Triangle = {
    val h = 1 << n
    (1 to h).map { x =>
      val pad = EMPTY * (h - x)
      pad + FILLED * (x * 2 - 1) + pad
    }.toArray
  }

  def scale(t: Triangle): Triangle = {
    val width      = t(0).length
    val widthNew   = width * 2 + 1
    val emptyWidth = (widthNew - width) / 2
    val emptyPart  = EMPTY * emptyWidth
    val top        = t.map(x => emptyPart + x + emptyPart)
    val bottom     = t.map(x => x + EMPTY + x)
    top ++ bottom
  }

  def scale(n: Int, t: Triangle): Triangle = n match {
    case 0 => t
    case n => scale(n - 1, scale(t))
  }

  def make(n: Int): Triangle = scale(n, base(5 - n))

  def printT(t: Triangle) = println(t.mkString("\n"))

  def main(args: Array[String]) {
    val n = scala.io.StdIn.readInt()
    printT(make(n))
  }
}
