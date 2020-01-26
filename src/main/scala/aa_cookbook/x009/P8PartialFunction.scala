package aa_cookbook.x009

object P8PartialFunction extends App {
  val div = (x: Int) => 42 / x

  val divide = new PartialFunction[Int, Int] {
    def apply(x: Int) = 42 / x
    override def isDefinedAt(x: Int) = x != 0
  }
  //divide(0)
  println(divide.isDefinedAt(0))

  val divide2: PartialFunction[Int, Int] = {
    case d: Int if d !=0 => 42 / d
  }

  val convert1to5 = new PartialFunction[Int, String] {
    val nums = Array("one", "two", "three", "four", "five")
    override def isDefinedAt(x: Int): Boolean = x>0 && x<6
    override def apply(v1: Int): String = nums(v1-1)
  }

  val convert6: PartialFunction[Int, String] = {
    case d: Int if d ==6 => "six"
  }

  val convert1to6 = convert1to5 orElse convert6
  println(convert6(6))

  println(List(0,1,2) collect divide2)


}
