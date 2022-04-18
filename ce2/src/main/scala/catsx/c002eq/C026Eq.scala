package catsx.c002eq

import cats.implicits.catsSyntaxEq
import catsx.c001show.Cat

object C026Eq extends App {
  import catsx.c002eq.C024Eq.StrictComparer.catEq

  val o1 = Option(Cat("Alice", 5))
  val o2 = Option(Cat("Alice", 5))

  /**
    * to run this, we need:
    * - have the === syntax
    * - have instance for Option
    * - have instance for Cat
    */

  val c = o1 === o2 // true
  println(c)
}

object C026Eq2 extends App {

  val o1 = Option(Cat("Alice", 5))
  val o2 = Option(Cat("Alice", 3))

  /** different implementation to enable relaxed comparing */
  import catsx.c002eq.C024Eq.RelaxedComparer.catEq

  val c = o1 === o2 // true
  println(c)
}
