package tools

object ArrayTools {

  /**
    * just check whether the given point belongs
    * to the array (just index check)
    */
  def ptInRange(a: Array[Array[Int]])(y: Int)(x: Int) =
    (x >= 0 && y >= 0 && y < a.length && x < a(0).length)

  /**
    * wrap to Optional for further flattening
    */
  def ptOrNone(a: Array[Array[Int]])(pt: (Int, Int)) = pt match {
    case (x, y) => Option.when(ptInRange(a)(y)(x))((x,y))
  }

  /**
    * just syntax
    */
  implicit class ArrayWithDefault(private val a: Array[Array[Int]]) extends AnyVal {
    def inRange(y: Int)(x: Int) = ptInRange(a)(y)(x)
    def orNone(y: Int)(x: Int) = ptOrNone(a)(x,y)
  }
}
