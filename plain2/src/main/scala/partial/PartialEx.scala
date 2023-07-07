package partial

import scala.collection.mutable

object PartialEx extends App {

  val a = Array("first", "second")

  val accessor: Int => String = a.apply
  
  val aa: mutable.ArraySeq.ofRef[String] = wrapRefArray(a)
  
  val f: Int => Option[String] = a.lift
  
  val x: String = a(0)
  val y: Option[String] = a.lift(1)
  val z: Option[String] = a.lift(2)
  pprint.pprintln(x)
  pprint.pprintln(y)
  pprint.pprintln(z)
  
  val l = Vector("a", "b")
  val fx: Int => Option[String] = l.lift
  val pf: PartialFunction[Int, String] = fx.unlift

//  val toInt: String => Int = (s: String) => s.toInt
//  val toIntOpt: String => Option[Int] = PartialFunction.fromFunction(toInt).lift
//  
//  pprint.pprintln(toIntOpt("55"))
//  pprint.pprintln(toIntOpt("55x"))
}
