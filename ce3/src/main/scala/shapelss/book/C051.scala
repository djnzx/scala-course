package shapelss.book

import shapeless.labelled.FieldType
import shapeless.syntax.SingletonOps
import shapeless.syntax.singleton._

import scala.reflect.runtime.universe.reify

object C051 extends App {

  def whatever(x: 4): Unit = {
    println(x)
  }

  whatever(4)


//  var x = 42.narrow
  var x: 42 = 42
  val someNumber: Int = 123
  val numCherries = "numCherries" ->> someNumber // KeyTag[String("numCherries"),Int]
  println(reify(numCherries))

  trait Cherries
  val numCh = 33.asInstanceOf[Int with Cherries]


}
