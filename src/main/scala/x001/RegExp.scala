package x001

import scala.util.matching.Regex

object RegExp extends App {
  println("%s, %s".format("Hello", "world"))

  val pattern = "[0-9]+".r
  val address = "228 47th street, Brooklyn, New York, 11220"
  val first = pattern.findFirstIn(address) // string
  val all = pattern.findAllIn(address) // iterator
  // all.foreach(println(_))
  println(all.toArray.mkString("[", ", ", "]"))
  val reg = new Regex("[0-9]+")
  val z = reg.findFirstMatchIn(address) // wrapped to some
  println(z.getClass.getName)
  println(z.getClass.getSimpleName)

  val addr = "Planet Earth, Ukraine, Kiev, Kolasa,"
  // Option -> Some/None
  val f1 = reg.findFirstIn(addr) // optional[string]
  val i = f1.getOrElse("-1")
  println(i.toInt + 10)
  println(f1)
  println(i)
  f1.foreach(print(_))

  val f2 = f1 match {
    case Some(s) => s"found $s"
    case None => "not found"
  }

  println(f2)
}
