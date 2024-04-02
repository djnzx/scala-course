package cats101.c002eq

import cats.Eq
import cats.implicits.catsSyntaxEq

object C025Eq extends App {

  println(5 == 5.001) // false

  def doubleComparerWithPrecision(d: Double): Eq[Double] = (x: Double, y: Double) => math.abs(x - y) <= d

  object precision0001 {
    implicit val d0001: Eq[Double] = doubleComparerWithPrecision(0.0001)
    println(5.0 === 5.0001) // true
    println(5.0 === 5.0002) // false
  }

  object precision01 {
    implicit val d01: Eq[Double] = doubleComparerWithPrecision(0.01)
    println(5.0 === 5.01) // true
    println(5.0 === 5.02) // false
  }

}
