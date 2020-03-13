package cats

//import cats.Functor._
import cats.instances.function._
import cats.syntax.functor._

object C070PartialUnification extends App {
  /**
    * “partial unification” is some kind of optional compiler behaviour, default, since 2.13
    *
    */
  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (y: Double) => y * 2

  /**
    * in order to compile this, compiler will search Functor for Function1
    *
    * trait Functor[F[_]] {
    *   def map[A, B](fa: F[A])(func: A => B): F[B]
    * }
    *
    * trait Function1[-A, +B] {
    *   def apply(arg: A): B
    * }
    * type F[A] = Int => A
    * type F[A] = A => Double
    *
    * this .map from cats. functor
    */
  val func3: Int => Double = func1.map(func2)

  val r: Double = func3(3) // 6.0
  println(r)
}
