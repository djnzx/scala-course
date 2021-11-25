package kubukoz.cats2

import cats.data.Ior
import cats.implicits._

object IoaApp extends App {

  val i1: Ior.Left[Int] = Ior.Left(1)
  val i2: Ior.Right[String] = Ior.Right("Hello")
  val i3: Ior.Both[Boolean, Double] = Ior.Both(true, 3.14)

  pprint.pprintln(i1)
  pprint.pprintln(i2)
  pprint.pprintln(i3)

}
