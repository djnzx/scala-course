package catsx

//import cats.Monoid
// Monoid[String]
import cats.Monoid
import cats.instances.string.catsKernelStdMonoidForString
//import cats.instances.string._ // for Monoid
// SemigroupSyntax: SemigroupSyntaxOps.|+|[A: Monoid]
//import cats.syntax.semigroup._
import cats.syntax.semigroup.catsSyntaxSemigroup

object C045Imports extends App {
  val r = "Scala" |+| " with " |+| "Cats"
  println(r)
}

import cats.instances.int._
import cats.instances.option._

object C045Imports2 extends App {
  val r = Option(1) |+| Option(2)
}

import cats.instances.map._

object C045Imports3 extends App {
  val map1 = Map("a" -> 1, "b" -> 2)
  val map2 = Map("b" -> 3, "d" -> 4)
  map1 |+| map2
}

object C045Imports4 extends App {
  def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A =
    values.foldRight(monoid.empty)(_ |+| _)
}
