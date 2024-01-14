package ctbartoz

import cats.data.Ior
import cats.implicits._

object IorExplore extends App {

  val a: Ior[String, Int] = Ior.Left("A")
  val b: Ior[String, Int] = Ior.Right(3)
  val c: Ior[String, Int] = Ior.Both("C", 13)

  val xs: (List[String], List[Int]) = List(a,b,c).separate
  pprint.pprintln(xs._1)
  pprint.pprintln(xs._2)

}
