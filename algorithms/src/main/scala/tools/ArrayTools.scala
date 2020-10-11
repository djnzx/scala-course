package tools

object ArrayTools {

  /**
    * just checks whether the given index belongs
    * to the 1D-array (just index check)
    */
  def indexInRange[A](idx: Int, a: Array[A]) = idx >= 0 && idx < a.length
  def indexOrNone[A](idx: Int, a: Array[A]) =
    Option.when(indexInRange(idx, a))(idx)
  
  /**
    * just checks whether the given point belongs
    * to the 2D-array
    */
  def pointInRange[A](a: Array[Array[A]])(y: Int)(x: Int) =
    indexInRange(y, a) &&
    indexInRange(x, a(0))  

  /**
    * wrap to Option for further flattening
    */
  def pointOrNone[A](a: Array[Array[A]])(pt: (Int, Int)) = pt match {
    case (x, y) => Option.when(pointInRange(a)(y)(x))((x,y))
  }

  /**
    * just syntax
    */
  implicit class ArraySyntax1D[A](private val a: Array[A]) extends AnyVal {
    def idxOrNone(x: Int) = indexOrNone(x, a)
  }
  implicit class ArraySyntax2D[A](private val a: Array[Array[A]]) extends AnyVal {
    def ptOrNone(y: Int)(x: Int) = pointOrNone(a)(x,y)
  }
}
