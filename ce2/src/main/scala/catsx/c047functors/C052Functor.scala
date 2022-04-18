package catsx.c047functors

import cats.Id
import cats.data.{Kleisli, Reader}

object C052Functor extends App {

  val f1 = (a: Int) => a + 1
  val f2 = (a: Int) => a * 2
  val f3 = (a: Int) => s"<$a>"

  val f41: Int => String = f1 andThen f2 andThen f3
  val f42: Kleisli[Id, Int, String] = Reader(f1) andThen Reader(f2) andThen Reader(f3)

  println(f41(1))
  println(f42(1)) // f42.run

}
