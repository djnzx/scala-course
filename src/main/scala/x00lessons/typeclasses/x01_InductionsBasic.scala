package x00lessons.typeclasses

object x01_InductionsBasic extends App {

  // example 1
  def length[A](list: List[A]): Int = list match {
    case Nil => 0
    case _ :: xs => 1 + length(xs)
  }

  // example 2
  def map[A, B](list: List[A], fn: A => B): List[B] = list match {
    case Nil => Nil
    case x :: xs => fn(x) :: map(xs, fn)
  }

  // example 3
  def fact(num: Int): Option[Int] = num match  {
    case nr if nr < 0 => None
    case 0 => Some(1)
    case nr => fact(nr - 1).map(_ * nr)
  }

  // example 4. no memoization! complexity n^n/2
  def fibo(num: Int): Option[Int] = num match {
    case n if n <= 0 => None
    case 1 => Some(1)
    case 2 => Some(1)
    case n => fibo(n - 1).map(_ + fibo(n-2).get)
  }

  // usage
  val l = List(11,22,33,44)
  println(fact(5))
  println(length(l))
  println(map(l, (i: Int) => i + 1))
  val n = 10
  println(s"fibo($n): ${fibo(n).get}")
}
