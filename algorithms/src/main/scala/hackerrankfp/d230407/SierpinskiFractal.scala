package hackerrankfp.d230407

object SierpinskiFractal extends App {

  type Triangle = Array[String]

  // 2^n implementation
  def pow2(n: Int): Int = 1 << n

  val EMPTY  = "_"
  val FILLED = "1"

  /** base(0)    => 1
    * base(1)    => _1_
    *               111
    * base(2)    => ___1___
    *               __111__
    *               _11111_
    *               1111111
    * ...
    */
  def base(n: Int): Triangle = {
    assert(n >= 0)
    val h = pow2(n)
    (1 to h).map { x =>
      val pad = EMPTY * (h - x)
      pad + FILLED * (x * 2 - 1) + pad
    }.toArray
  }

  val BASE1 = base(1)
//  printT(base(2))

  def printT(t: Triangle) = println(t.mkString("\n"))

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

  /** n = 0 - base(5) combined 0
    * n = 1 - base(4) combined once
    * n = 2 - base(3) combined twice
    * n = 3 - base(2) combined 3
    * n = 4 - base(1) combined 4
    */
  def make(n: Int): Triangle = scale(n, base(5 - n))

  printT(make(4))

}
