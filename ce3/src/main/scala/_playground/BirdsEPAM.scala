package _playground

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BirdsEPAM extends AnyFunSuite with Matchers {

  // Having an array of bird sightings
  // where every element represents a bird type id,
  // determine the id of the most frequently sighted type.
  // If more than 1 type has been spotted that maximum amount, return the smallest of their ids.
  // Example
  // arr = [1, 1, 2, 2, 3]
  // There are two each of types 1 and 2, and one sighting of type 3.
  // Pick the lower of the two types seen twice: type 1.

  // 1. group by id: Map[id, count]
  // 2. group by count: Map[count, min id]

  def pick(xs: List[Int]): Option[Int] =
    xs.groupMapReduce(identity)(_ => 1)(_ + _)
      .groupMapReduce { case (_, c) => c } { case (id, _) => id }(_ min _)
      .reduceOption[(Int, Int)] { case (g1 @ (c1, _), g2 @ (c2, _)) => if (c1 > c2) g1 else g2 }
      .map { case (_, id) => id }

  test("1") {
    pick(List(11, 11, 11, 10, 10, 10, 5, 2, 2)) shouldEqual Some(10)
  }

  test("2") {
    pick(List(11, 11, 11, 10, 10, 5, 2, 2)) shouldEqual Some(11)
  }

  test("3") {
    pick(List(2)) shouldEqual Some(2)
  }

  test("4") {
    pick(List()) shouldEqual None
  }

}
