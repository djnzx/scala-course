package _udemy.scala_beginners.lectures.part2oop._apply_unapply

import scala.collection.immutable.List
import scala.util.Random

object Unapply extends App {
  object CustomerID {
    def apply(name: String) = s"$name--${Random.nextLong}"

    def unapply(customerID: String): Option[String] = {
      val stringArray: Array[String] = customerID.split("--")
      if (stringArray.tail.nonEmpty) Some(stringArray.head) else None
    }
  }

  // apply
  val customer1ID = CustomerID("Sukyoung")  // Sukyoung--182497634113904576
  println(customer1ID) // string: Sukyoung--182497634113904576

  // unapply explicit
  val name = CustomerID.unapply(customer1ID).get
  println(s"unapply explicit: $name")

  // unapply implicit #1 (pattern matching)
  val CustomerID(x) = customer1ID
  println(s"unapply implicit #1: $x")

  // unapply implicit #2 (pattern matching with optional support)
  customer1ID match {
    case CustomerID(n) => println(s"unapply implicit #2: $n")  // unapply called under the hood
    case _ => println("Could not extract a CustomerID")
  }

  val s =  List.fill(10)((Random.nextInt('Z' - 'A') + 'A').toChar).mkString
  println(s)
}
