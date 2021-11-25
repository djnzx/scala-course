package kubukoz.cats2

import cats.data.NonEmptyList
import cats.implicits._

object Nel extends App {

  val n1: NonEmptyList[Int] = NonEmptyList(1, List(2, 3, 4, 5))
  val n2 = n1.filter(_ > 1)

  val x1: Option[NonEmptyList[Int]] = List(1, 2, 3).toNel
  val x2: Option[NonEmptyList[Int]] = List().toNel
  pprint.pprintln(x1)
  pprint.pprintln(x2)

}
