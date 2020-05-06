package catsx

import cats.instances.function._ // functor
import cats.syntax.functor._     // map

object C058Functor extends App {
  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => s"$a!"

  // scala - sysntax based on Function2[A,B]
  val func4a: Int => String = func1 andThen func2 andThen func3
  // cats
  val func4b: Int => String = func1 map func2 map func3

  val r1: String = func4a(127) // 256!
  val r2: String = func4b(127) // 256!
  println(r1)
  println(r2)
}
