package aa_cookbook.x001

object ToMethods extends App {
  println("42".toInt+1)
  println("3.5".toDouble+1)
  println(!"true".toBoolean)
  println("true".toCharArray.mkString("[",",","]"))
}
