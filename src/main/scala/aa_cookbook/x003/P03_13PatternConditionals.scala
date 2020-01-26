package aa_cookbook.x003

object P03_13PatternConditionals extends App {
  val kind1 = (a: Int) => a match {
    case _ if a % 2 == 0 => "even"
    case _ if a % 2 != 0 => "odd"
  }

  val kind2 = (a: Int) => a match {
    case x if x % 2 == 0 => "even"
    case x if x % 2 != 0 => "odd"
  }

  println(kind1(1))
  println(kind1(2))
  println(kind2(3))
  println(kind2(4))

}
