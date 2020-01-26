package cats

import cats.Functor
import cats.instances.function._ // functor
import cats.instances.option._   // for Functor
import cats.instances.list._     // for Functor
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
object C059Functor extends App {
  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => a + "!"
  val func4a = func1 map func2 map func3         // cats

  def doMath[F[_]]                // abstraction over any type
  (origin: F[Int])                // data to process we dont know the type
  (implicit functor: Functor[F])  // implicit instance which is aware how to work with that type
  : F[String] =                   // return type
    origin.map(func4a)            // implementation !

  val r1: Option[String] = doMath(Option(20))
  val r2: List[String] = doMath(List(1, 2, 3))
  println(r1)
  println(r2)
}
