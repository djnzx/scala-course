package catsx

import cats.{Foldable, Monoid}
import cats.instances.list._
import cats.instances.vector._
import cats.instances.int._
import cats.instances.string._

object C169Foldable extends App {
  val data: List[Int] = 1 to 10 toList

  // extract instance from cats
  val fl = Foldable[List]

  // allMatch
  // manually written
  def allMatch1[A](xs: List[A])(p: A => Boolean): Boolean =
    xs.foldLeft(true)((acc, a) => acc && p(a))

  // built-in
  def allMatch2[A](xs: List[A])(p: A => Boolean): Boolean = xs.forall(a => p(a))

  // noneMatch
  // manually written
  def noneMatch1[A](xs: List[A])(p: A => Boolean): Boolean =
    xs.foldLeft(true)((acc, a) => acc && !p(a))

  // built-in
  def noneMatch2[A](xs: List[A])(p: A => Boolean): Boolean = xs.forall(a => !p(a))

  // anyMatch
  // manually written
  def anyMatch[A](xs: List[A])(p: A => Boolean): Boolean =
    xs.foldLeft(false)((acc, a) => acc || p(a))

  // from cats
  def anyMatch2[A](xs: List[A])(p: A => Boolean): Boolean =
    fl.exists(xs)(p)

  fl.maximumOption(data)
  val found: Option[Int] = fl.find(data)(_ == 5)

  // we can do that because of monoid (sum)
  val combined1: Int = fl.combineAll(data)
  println(combined1)

  // we can provide the custom monoid for multiplication
  val combined2: Int = fl.combineAll(data)(new Monoid[Int] {
    def empty: Int = 1
    def combine(x: Int, y: Int): Int = x*y
  })
  println(combined2)

  val combined3 = fl.foldMap(data)(n => s"[$n]")
  println(combined3)

  /** plain Scala2 syntax */
  val ff: Foldable[({
    type x[α] = List[Vector[α]]
  })#x] = Foldable[List] compose Foldable[Vector]

  /** Kind Projector Plugin */
  val ff2: Foldable[Lambda[α => List[Vector[α]]]] = Foldable[List] compose Foldable[Vector]
  val ff3: Foldable[λ[α => List[Vector[α]]]] = Foldable[List] compose Foldable[Vector]

  val dataComposed = List(Vector(1,2,3), Vector(4,5,6))
  val combined4 = ff.combineAll(dataComposed)
  println(combined4)
}
