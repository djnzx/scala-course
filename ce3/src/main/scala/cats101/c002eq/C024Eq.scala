package cats101.c002eq

import cats.Eq
import cats.implicits.catsSyntaxEq
import cats101.c001show.Cat

object C024Eq extends App {
  /**
    * the problem, ugly mistake
    * the result always will be empty list
    * because we can't compare `x` and `Option(x)`
    */
  val r1: List[Option[Int]] = List(1, 2, 3).map(x => Option(x)).filter(x => x == 1)
  // will not compile
//    val r2: List[Option[Int]] = List(1, 2, 3).map(x => Option(x)).filter(x => x === 1)

  {
    val b1 = 1 === 1 // trur
    // will not compile
    //    val b2 = 1 === "1"
  }

  val cat1: Cat = Cat("Alice", 3)
  val cat2: Cat = Cat("Alice", 4)

  object example0{
    val b1 = cat1 == cat2 // false
    // will not compile
    //    val b2 = cat1 === cat2
  }

  /** strict comparer */
  object StrictComparer {
    implicit val catEq: Eq[Cat] = (x: Cat, y: Cat) => x == y
  }
  {
    import StrictComparer.catEq
    val b1 = cat1 === cat2 // false
    println(b1)
  }

  /** name only comparer */
  object RelaxedComparer {
    implicit val catEq: Eq[Cat] = (x: Cat, y: Cat) => x.name == y.name
  }
  {
    import RelaxedComparer.catEq
    val b1 = cat1 === cat2 // true
    println(b1)
  }
}
