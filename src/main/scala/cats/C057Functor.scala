package cats

import scala.language.higherKinds
import cats.Functor
import cats.instances.list._ // for Functor
import cats.instances.option._ // for Functor

/**
  * `trait Functor[F[_]]` {
  *   def map[A, B](fa: F[A])(f: A => B): F[B]
  * }
  *
  */
object C057Functor extends App {
  val list1: List[Int] = List(1, 2, 3)

  val function = (x: Int) => x * 2

  val functor: Functor[List] = Functor[List] // peek the functor from the implicits

  val list2 = functor
              .map(list1)(function)  // provide the kind of type of data to operate with
                                    // and provide the functor

  val lifted: List[Int] => List[Int] = functor.lift(function)

  val list2a = list1.map(function)   // this will be called under the hood
  val list2b = lifted(list1)

  println(list1)
  println(list2)
  println(list2a)
  println(list2b)

  val option1 = Option(123)
  val option2 = Functor[Option].map(option1)(_.toString)

}
