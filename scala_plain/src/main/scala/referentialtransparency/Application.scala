package referentialtransparency

object external {
  def func(x: Int): Int = {
    println(s"x=$x")
    x
  }
}

object App1 extends App {
  val r: (Int, Int) = (external.func(1), external.func(1))
  pprint.log(r)
}

object App2 extends App {
  val x: Int = external.func(1)
  val r: (Int, Int) = (x, x)
  pprint.log(r)
}
