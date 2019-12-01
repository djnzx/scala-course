package x71cats

import cats.Eq
import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._
import x71cats.C020.Cat

object C024Eq extends App {
  // ugly mistake
  List(1, 2, 3).map(x => Option(x)).filter(item => item == 1)

  val eqInt = Eq[Int]
//  eqInt.eqv(123, "234")
  println(1 === 1)
//  println(1 === Cat("Alice", 3))

  // the error will be detected immediately
//  List(1, 2, 3).map(x => Option(x)).filter(item => item === 1)

  val cat1 = Cat("Alice", 3)
  val cat2 = Cat("Alice", 3)
  println(cat1 == cat2)    // true
//  println(cat1 === cat2) // won't compile
  implicit val cat_eq: Eq[Cat] = (x, y) => x.age == y.age && x.name == x.name
  println(cat1 === cat2)  // true

}
