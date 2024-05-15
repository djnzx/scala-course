package winitzki

import cats.implicits.catsSyntaxEitherId
import scala.util.Try

class WellKnownVarianceExamples extends Base {

  /** Covariant things:
    * [[List]]
    * [[Vector]]
    * [[Set]]
    * [[Option]][+A]
    *   getOrElse[B >: A](default: => B): B
    *   contains[A1 >: A](elem: A1): Boolean
    *
    * [[Either]][+E,+A]
    *   flatMap[E1 >: E, A2](f: A => Either[E1, A2]): Either[E1, A2]
    *   flatten[E1 >: E, A1](implicit ev: A <:< Either[E1, A1]): Either[E1, A1]
    *   joinLeft [E1 >: E, A1 >: A, E2(implicit ev: E1 <:< Either[E2, A1]): Either[C, B1]
    *   joinRight[E1 >: E, A1 >: A, A2](implicit ev: A1 <:< Either[E1, A2]): Either[E1, A2]
    *
    * [[Try]][+A]
    *   getOrElse[A0 >: A](default: => A0): A0
    *
    *  [[Ordering]][T]
    *   def max[U <: T](x: U, y: U): U = if (gteq(x, y)) x else y
    *
    * [[PartialFunction]][-A, +B]
    *   orElse     [A1 <: A, B1 >: B](that: PartialFunction[A1, B1]): PartialFunction[A1, B1]
    *    applyOrElse[A1 <: A, B1 >: B](x: A1, default: A1 => B1): B1
    */

  class Animal
  class Dog      extends Animal
  class Shepherd extends Dog
  class Laika    extends Dog

  class Cat    extends Animal
  class Alice  extends Cat
  class Barsik extends Cat

  val x: Either[Cat, Shepherd] = (new Laika).asRight[Alice] // Either[Alice, Laika]
    // basic semantics A => B
    // so type `B` without any annotation
    // we have the same behavior as option.getOrElse
    // flatMap[E0 >: E, A2](f: A => Either[E0, A2]): Either[E0, A2]
    .flatMap(_ => (new Shepherd).asRight[Barsik]) // Either[Cat, Shepherd]

  val y: Dog = Option(new Laika)
    .getOrElse(new Shepherd)

  val z: Dog = Try(new Laika)
    .getOrElse(new Shepherd)

}
