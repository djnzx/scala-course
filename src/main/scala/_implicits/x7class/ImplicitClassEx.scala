package _implicits.x7class

object ImplicitClassEx extends App {

  // class name doesn't matter
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

  // (type enrichment, class name doesn't matter), direct marking class
  implicit class IntExtra(origin: Int) {
    def sayHello = println(s"Hello: $origin")
  }

  class IntExtra2(origin: Int) {
    def sayHello2 = println(s"Hello2: $origin")
  }
  // wiring via conversion function
  implicit val intToExtra2 = (i: Int) => new IntExtra2(i)

  // here 1 becomes the instance of IntExtra
  1.sayHello
  // here 1 becomes the instance of IntExtra2
  10.sayHello2


}
