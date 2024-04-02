package cats101

import cats.Monoid
import cats.instances.int._
import cats.instances.list._
import cats.instances.string._
import cats.instances.invariant._ // Semigroupal
import cats.syntax.apply._
import cats.syntax.semigroup._ // |+|

object C148Semi extends App {
  case class Cat(
                  name: String,
                  yearOfBirth: Int,
                  favoriteFoods: List[String]
                )

  val tupleToCat: (String, Int, List[String]) => Cat = Cat.apply _

  val catToTuple: Cat => (String, Int, List[String]) = cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

  implicit val catMonoid: Monoid[Cat] = (
    Monoid[String],
    Monoid[Int],
    Monoid[List[String]]
    ).imapN(tupleToCat)(catToTuple)

  val garfield = Cat("Garfield", 1978, List("Lasagne"))
  val heathcliff = Cat("Heathcliff", 1988, List("Junk Food"))
  val combined: Cat = garfield |+| heathcliff
}
