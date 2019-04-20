package x010

import x010.EnumDays.EnumDays

import scala.collection.immutable.HashMap

object C10_26 extends App {
  val today = EnumDays.Sat
  EnumDays.values.foreach(println)

  val m = Map(11->EnumDays.Mon, 22->EnumDays.Tue) // scala.collection.immutable.Map[Int,EnumDays.Value]
  val m1 = new HashMap[Int, EnumDays]
  val m2 = m1 + (11->EnumDays.Mon)
  println(m2)

  case class Cat(name: String)
  val m3 = new HashMap[Int, Cat]

}
