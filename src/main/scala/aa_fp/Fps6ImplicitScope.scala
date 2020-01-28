package aa_fp

object Fps6ImplicitScope extends App {

  def inc(origin: Int)(implicit delta: Int): Int = origin + delta

  object Scope1 {
    implicit val delta1: Int = 1
  }

  object Scope2 {
    implicit val delta2: Int = 2
  }

  import Scope1._

  println(inc(5)) // 6

  def nested() = {
    println(inc(5)) // 6
  }

  nested()

}
