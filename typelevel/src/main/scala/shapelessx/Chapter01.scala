package shapelessx

import shapeless._
import pprint.{pprintln => println}

/**
  * https://books.underscore.io/shapeless-guide/shapeless-guide.html#introduction
  */
object Chapter01 extends App {
  case class Employee(name: String, number: Int, manager: Boolean)
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  def employeeCsv(e: Employee): List[String] =
    List(e.name, e.number.toString, e.manager.toString)
  def iceCreamCsv(c: IceCream): List[String] =
    List(c.name, c.numCherries.toString, c.inCone.toString)

  
  // unpack case classes to Heterogeneous typed lists
  val genericEmployee: String :: Int :: Boolean :: HNil = Generic[Employee].to(Employee("Dave", 123, false))
  val genericIceCream: String :: Int :: Boolean :: HNil = Generic[IceCream].to(IceCream("Sundae", 1, false))

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] = {
    val g0: String = gen(0)
    val g1: Int = gen(1)
    List(g0, g1.toString, gen(2).toString)
  }

  val csv1 = genericCsv(genericEmployee)
  val csv2 = genericCsv(genericIceCream)

  val repr: String :: Int :: Boolean :: HNil = "Hello" :: 123 :: true :: HNil
  
}
