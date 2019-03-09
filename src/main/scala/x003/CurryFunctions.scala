package x003

object CurryFunctions extends App {

  def func3(a1: Int, a2: Int)(a3: Int): Int = {
    (a1 + a2) * a3
  }

  println(func3(1,2)(3))
  val func2 = func3(1,2)(_)

  println(func2(4))
  println(func2(5))
}
