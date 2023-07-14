package catsx.c077monads

import cats.Monad

object C084 extends App {

  // map via flatMap
  trait Monad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value) { a => pure(func(a)) }
  }

  //  import cats.Monad

  import cats.instances.option._

  val opt1: Option[Int] = Monad[Option].pure(3)
  val opt2: Option[Int] = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  val opt3: Option[Int] = Monad[Option].map(opt2)(a => 100 * a)

  import cats.instances.list._

  val list1: List[Int] = Monad[List].pure(3)
  val list2: List[Int] = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
  val list3: List[Int] = Monad[List].map(list2)(a => a + 123)

  import cats.instances.future._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent._
  import scala.concurrent.duration._

  val fm = Monad[Future]

  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  Await.result(future, 1.second)

  import cats.syntax.applicative._

  1.pure[Option]
}
