package _implicits.x3typeclasses

object TypeClassApp extends App {

  // induction length(), recursive
  def length[A](list: List[A]): Int =
    list match {
      case Nil => 0
      case _ :: tail => length(tail) + 1
    }
  // induction map(), recursive
  def map[A, B](list: List[A], f: A => B): List[B] =
    list match {
      case Nil => Nil
      case head :: tail => f(head) :: map(tail, f)
    }

  println(length(List(1)))     // 1
  println(length(List(1,2,3))) // 3
  println(map(List(1,2,3), (x: Int) => x * 2 )) // 2, 4, 6
  // 1. Typeclass = associate functionality with class
  // 2. Write generic trait Foo[A] { ... }
  // 3. Rely on implicit resolution


}
