package ef

object FactApp0 extends App {

  def fact0(n: Int): Int = n match {
    case 0 => 1
    case n => n * fact0(n - 1);
  }

  def fact2(n: Int, a: Int): Int = n match {
    case 0 => a
    case n => fact2(n - 1, a * n)
  }

  def fact2(n: Int): Int = fact2(n , 1)

  println(fact0(5))
  println(fact2(10000000))

}
