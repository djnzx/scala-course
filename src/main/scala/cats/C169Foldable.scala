package cats

import cats.instances.list._
import cats.instances.int._

object C169Foldable extends App {
  val data = 1 to 10 toList

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

  val f = Foldable[List]
  f.maximumOption(data)

}
