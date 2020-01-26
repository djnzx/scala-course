package cats

import cats.instances.function._ // functor
import cats.syntax.functor._     // map

object C058Functor extends App {
  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => s"$a!"
  val func4a = func1 map func2 map func3         // cats
  val func4b = func1 andThen func2 andThen func3 // scala

  val r1: String = func4a(127)
  val r2: String = func4b(127)
  println(r1)
  println(r2)
}
