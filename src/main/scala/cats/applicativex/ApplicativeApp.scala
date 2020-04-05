package cats.applicativex

import cats.{Applicative, Apply, Functor}
import cats.instances.list._
import cats.instances.option._

object ApplicativeApp extends App {
  val data = List(1,2,3)
  Functor[List].map(data)(x => x + 1)

  // it
  val al: Applicative[List] = Applicative[List]
  val l1: List[Int] = al.pure(1)
  val l2: List[List[Int]] = al.pure(data) // List(List(1,2,3))

  val someF: Option[Int => Long] = Some(x => x.toLong + 1L)
  val noneF: Option[Int => Long] = None
  val someInt: Option[Int] = Some(3)
  val noneInt: Option[Int] = None

  Apply[Option].ap(someF)(someInt)
  /**                ^       ^
    *              func    value
    *  if something is wrong
    */

  /**
    * List(1,2,3) => List(11, 12, 13)
    */
  val mapped_by_functor =
    Functor[List].map(data)(_+10)
  println(s"Mapped by Functor: $mapped_by_functor")

  val mapped_by_apply =
    Apply[List].ap(List[Int => Int](_+10, _+20))(data)
  println(s"Mapped by Apply: $mapped_by_apply")

  /**
    * dependent
    */
  for {
    x <- List(1, 2, 3)
    y <- List(x+10, x+11)
  } yield y // List(11,12,13, 21,22,23)
  /**
    * independent
    */
  for {
    first <- Some("Bob")
    last <- Some("Axel")
  } yield s"$first $last"
  /**
    * another example
    */
  val add : Int => Int => Int = (x: Int) => (y: Int) => x + y
  val add1: Int => Int = add(1)
  val A1: List[Int => Int] = List(10,20).map(add) // two functions _+10 and _+20
  // Applicative has map2:
  // def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z]
  val result1: List[Int] = Applicative[List].ap(A1)(List(4,5)) // List(14, 15, 24, 25)

}
