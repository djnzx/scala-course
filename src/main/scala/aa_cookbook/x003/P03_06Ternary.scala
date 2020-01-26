package aa_cookbook.x003

object P03_06Ternary extends App {
  val abs = (a: Int) => if (a<0) -a else a

  println(abs(5))
  println(abs(-5))
  println(abs) // function

}
