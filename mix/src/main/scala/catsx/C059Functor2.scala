package catsx

import cats.Functor
import cats.instances.function._
import cats.syntax.functor._     // map
/**
  * implicit class FunctorOps[F[_], A](src: F[A]) {
  * def map[B]
  * (func: A => B)
  * (implicit functor: Functor[F]): F[B] =
  *         functor.map(src)(func)
  * }
  *
  * after that, compiler can insert .map wherever it can find the implicit
  * declaration for the functor
  */
object C059Functor2 extends App {
  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => s"$a!"
  val func4a = func1 map func2 map func3

  final case class Box[A](value: A)
  val b1: Box[Int] = Box[Int](123)
  val b3: Box[Option[Int]] = Box(Option(13))

  implicit val box_functor: Functor[Box] = new Functor[Box] {
    override def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
  }

  implicit val opt_functor: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  val b1m: Box[String] = b1.map(func4a)
  println(b1m)
  val b2m = b3.map(_.map(_ + 1))
  println(b2m)

}
