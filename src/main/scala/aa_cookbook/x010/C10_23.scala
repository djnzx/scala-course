package aa_cookbook.x010

object C10_23 extends App {
  val men = List("Sasha", "Dima", "Alex", "Victor")
  val women = List("Nata", "Ira", "Lena")
  val pairs = men zip women
  println(pairs)
  for ((man, woman) <- pairs) {
    println(s"MAN $man, WOMAN: $woman")
  }

}
