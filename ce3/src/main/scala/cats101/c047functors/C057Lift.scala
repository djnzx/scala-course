package cats101.c047functors

import cats.Functor

object C057Lift extends App {

  val data = List(1, 2, 3)

  val f: Int => Int = x => x * 10

  /** without lifting */
  val data2a = data.map(f)
  println(data2a)

  /** by using lifting */
  val lifted: List[Int] => List[Int] = Functor[List].lift(f)
  val data2b = lifted(data)
  println(data2b)
}
