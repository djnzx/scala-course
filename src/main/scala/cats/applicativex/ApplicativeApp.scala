package cats.applicativex

import cats.{Applicative, Apply, Functor}
import cats.instances.list._
import cats.instances.option._

/**
  * http://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
  * https://softwaremill.com/images/uploads/2016/04/blog-applicative-01.2bb49969.png
  * https://typelevel.org/cats/typeclasses/applicative.html
  * https://medium.com/@tobiasvonwennergren/applicative-functor-the-power-of-less-6d7ec758b87a
  * https://softwaremill.com/applicative-functor/
  * https://kseo.github.io/posts/2014-01-26-swtiching-from-monads-to-applicative-functors.html
  * https://medium.com/@lettier/your-easy-guide-to-monads-applicatives-functors-862048d61610
  */
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
    * independent, Monad version
    */
  val opt1 = Option("Bob")
  val opt2 = Option("Axel")
  for {
    first <- opt1
    last <- opt2
  } yield s"$first $last"
  /**
    * applicative version
    */
  Apply[Option].map2(opt1, opt2)((a, b) => s"$a $b")

  val add : Int => Int => Int = (x: Int) => (y: Int) => x + y
  val add1: Int => Int = add(1)
  val A1: List[Int => Int] = List(10,20).map(add) // two functions _+10 and _+20
  // Applicative has map2:
  // def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z]
  val result1: List[Int] = Applicative[List].ap(A1)(List(4,5)) // List(14, 15, 24, 25)
  def map2_via_ap[F[_]: Applicative, A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] = {
    // extract Applicative[F] instance from cats implementation
    val af: Applicative[F] = Applicative.apply[F]
    // currying to use f: (A, B) => Z
    def f_abz(a: A)(b: B): Z = f(a,b)
//    or:
//    val f_abz: A => B => Z =
//               a => b => f(a,b)
    /**
      * we use map(fa: F[A])(f: A => B): F[B]
      * to map F[A] by f: A => B => Z (partially)
      * and get F[B => Z]
      */
    val fbz: F[B => Z] = af.map(fa)(f_abz)
    //                         F[A]  A=>B
    af.ap(fbz)(fb)
  }
  /**
    * ff: F[A => B]
    * mostly comes from partial application
    * or .pure lifting
    */
}
