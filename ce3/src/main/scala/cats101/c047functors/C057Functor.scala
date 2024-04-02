package cats101.c047functors

import cats.Functor

/**
  * `trait Functor[F[_]]` {
  * def map[A, B](fa: F[A])(f: A => B): F[B]
  * }
  *
  */
object C057Functor extends App {
  val source: List[Int] = List(1, 2, 3)
  val function = (x: Int) => x * 2

  // peek the functor from the implicits
  val functor3: Functor[List] = implicitly[Functor[List]] // syntax #1
  val functor2: Functor[List] = Functor.apply[List] // syntax #2
  val functor: Functor[List] = Functor[List] // syntax #3

  // Scala embedded List[A].map[A=>B]
  val list2a: List[Int] = source.map(function) // this will be called under the hood
  // cats functor
  val list2b: List[Int] = functor.map(source)(function)
  // create lifter
  val lifter: List[Int] => List[Int] = functor.lift(function)
  // use lifter
  val list2c: List[Int] = lifter(source)

  println(s"Source: $source")
  println(s"list2a: $list2a")
  println(s"list2b: $list2b")
  println(s"list2c: $list2c")

  val option1: Option[Int] = Option(123)
  val ofunctor: Functor[Option] = Functor[Option]
  val option2: Option[String] = ofunctor.map(option1)(_.toString)

}
