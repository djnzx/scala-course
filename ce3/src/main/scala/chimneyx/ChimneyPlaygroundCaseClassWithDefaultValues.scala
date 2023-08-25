package chimneyx

import io.scalaland.chimney.dsl._

object ChimneyPlaygroundCaseClassWithDefaultValues extends App {

  case class A(x1: Int)
  case class B(y1: Int = -13)

  val a = A(33)

  val b: B = a
    .into[B]
    .withFieldRenamed(_.x1, _.y1)
    .transform

  pprint.pprintln(b)

}
