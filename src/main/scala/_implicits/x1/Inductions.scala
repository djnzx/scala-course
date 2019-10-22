package _implicits.x1

object Inductions extends App {
  def fact(num: Int): Option[Int] = num match  {
    case nr if nr < 0 => None
    case 0 => Some(1) // base case
    case nr => fact(nr - 1).map(_ * nr) // inductive step
  }

  def length[A](list: List[A]): Int = list match {
    case Nil => 0
    case _ :: xs => 1 + length(xs)
  }

  def map[A, B](list: List[A], fn: A => B): List[B] = list match {
    case Nil => Nil
    case x :: xs => fn(x) :: map(xs, fn)
  }

  val l = List(11,22,33,44)
  println(fact(5))
  println(length(l))
  println(map(l, (i: Int) => i + 1))
}
