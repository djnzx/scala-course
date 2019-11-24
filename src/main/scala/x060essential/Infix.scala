package x060essential

object Infix {
  /**
    * Note that a b c d e is equivalent
    * to a.b(c).d(e),
    * not a.b(c, d, e).
    */

  def bad1: Nothing = throw new Exception("Error")
  def bad2: Null = null
  def ambiguity1: Any = if (1>2) 3

  case class PersonZ(fname: String, lname: String) {
    def this(fname: String) = {
      this(fname, "")
    }
  }

  new PersonZ("a","b")
  val p = new PersonZ("c")
}
