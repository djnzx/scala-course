package catsx

import cats.Eq
import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._
import C020Show.Cat

object C024Eq extends App {
  /**
    * the problem, ugly mistake
    * the result always will be empty list
    * because we can'c compare `x` and `Option(x)`
    */
  val r: List[Option[Int]] = List(1, 2, 3).map(x => Option(x)).filter(x => x == 1)

  val eqInt = Eq[Int]
  println(1 === 1) // true
  println("----")
  /**
    * these examples even will not compile
    */
//  val req1: Boolean = eqInt.eqv(123, "234")
//  val req2: Boolean = 1 === "1"
//  val req3: Boolean = 1 === Cat("Alice", 3)
//  List(1, 2, 3).map(x => Option(x)).filter(item => item === 1)

  val cat1: Cat = Cat("Alice", 3)
  val cat2: Cat = Cat("Alice", 4)
  println(cat1 == cat2)                  // false

  // we can define custom equalizer
  val eqCatNameOnly: Eq[Cat] = (c1, c2) => c1.name == c2.name
  println(eqCatNameOnly.eqv(cat1, cat2)) // true

  // this will explode in runtime !!!
//  println(cat1 === cat2)

  // or we can put implicit equalizer into scope
  implicit val eqCat: Eq[Cat] = (c1, c2) => c1.age == c2.age && c1.name == c2.name
  println(cat1 === cat2)                 // false
}
