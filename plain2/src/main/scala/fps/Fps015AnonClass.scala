package fps

import scala.annotation.unused
import scala.util.{Try, Using}

object Fps015AnonClass extends App {

  trait Car {
    val manuf: String
    val engine: Int
  }

  val c = new Car {
    val manuf = "Chrysler"
    val engine = 5700
  }

  case class Transform[A, B](f: A => B)
  val t1a: Transform[String, Int] = Transform((s: String) => s.length)
  val t1b: Transform[String, Int] = Transform { s: String => s.length }
  val t2a: Transform[String, String] = Transform((s: String) => s.toUpperCase)
  val t2b: Transform[String, String] = Transform { s: String => s.toUpperCase }
  val t2c: Transform[String, String] = Transform { (s: String) => s.toUpperCase }

  val r1: Int = t1a.f("Hello")
  val r2: String = t2b.f("hello")

  case class PartNumber(vendor: String)(@unused number: String)
  val pn = PartNumber("Tinken")("5707")

  val r: Try[String] = Using(scala.io.Source.fromFile("1.txt"))(s => s.getLines().mkString("\n"))


}
