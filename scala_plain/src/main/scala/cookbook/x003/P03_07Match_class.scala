package cookbook.x003

object P03_07Match_class extends App {
  def getClassName1(x: Any): String = {
    x match {
      case s: String => s"String $s"
      case i: Int => s"Int $i"
      case b: Boolean => s"Boolean $b"
      // case a: Any => s"Other $a"
      case default => "possible exception caught!"
      // we always should cover corner cases with keyword default
    }
  }

  def getClassName2 = (x: Any) => {
    "1"
  }

  val getClassName3 = (x: Any) => {
    "1"
  }

  println(getClassName1("Alex"))
  println(getClassName1(42))
  println(getClassName1(true))
  println(getClassName1(1L)) // scala.MatchError
}
