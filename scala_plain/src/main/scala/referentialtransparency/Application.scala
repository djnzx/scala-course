package referentialtransparency
import pprint.{pprintln => println}

object external {
  def times2(x: Int): Int = {
    println(s"x=$x")
    x * 2
  }
}

object App1 extends App {
  val r: (Int, Int) = (external.times2(1), external.times2(1))
  
  println(r)
}

object App2 extends App {
  val x: Int = external.times2(1)
  val r: (Int, Int) = (x, x)
  
  println(r)
}
