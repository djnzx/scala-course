package catsx

import cats.{Id, Monad}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.applicative._
import cats.instances.option._

//import cats.Id

object C088IdentityMonad extends App {

  def sumSq[F[_]: Monad](ma: F[Int], mb: F[Int]): F[Int] = for {
    a <- ma
    b <- mb
  } yield a * a + b * b

  val r: Option[Int] = sumSq(Option(3), Option(4))
  println(s"r = ${r}")

  /**
    * Identity monad
    * that's when we need a Monad
    * but we have a value only
    * Identity monad that's a lifter
    * from the plain value A to the monad Id[A]
    *
    * Id allows us to call our monadic method using plain values
    *
    * Identity monad is a good way to test some ideas and test futures
    *
    */

  val r3: Id[Int] = sumSq[Id](3, 4)
  sumSq(3: Id[Int], 4: Id[Int])

  val r4: Id[Int] = r3.map(x => x + 100)
  println(r4)

  val r5: Id[Int] = 1.pure[Id]
  val r6: Id[Int] = Monad[Id].pure(4)

}
