package fact

object FactApp extends App {

  def fact0(n: Int): Int =
    n match {
      case 1 => 1
      case _ => n * fact0(n - 1)
    }

  def fact(n: Int, ac: Int): Int =
    n match {
      case 1 => ac
      case _ => fact(n - 1, n * ac)
    }

  def fact(n: Int): Int =
    fact(n, 1)

  def sum(xs: List[Int]): Int =
    xs match {
      case Nil          => 0
      case head :: tail => head + sum(tail)
    }

  def sum2(xs: List[Int], ac: Int = 0): Int =
    xs match {
      case Nil          => ac
      case head :: tail => sum2(tail, head + ac)
    }

//  println(fact(5))
  println(sum(List(1, 10, 100)))
  println(sum2(List(1, 10, 100)))

}
