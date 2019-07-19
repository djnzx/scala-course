package _implicits.x1

object ImplicitApp1 extends App {

  implicit class StringExtra(origin: String) {
    def inc() = origin.map(c => (c + 1).toChar)
    def dec() = origin.map(c => (c - 1).toChar)
  }

  val s1 = "Hello"
  val s2 = s1.inc()
  val s3 = s2.dec()
  println(s1)
  println(s2)
  println(s3)

}
