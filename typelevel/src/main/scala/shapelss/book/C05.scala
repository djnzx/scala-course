package shapelss.book

import shapeless.labelled.FieldType
import shapeless.labelled.KeyTag
import shapeless.syntax.SingletonOps
import shapeless.syntax.singleton._
import shapeless.labelled.field

import scala.reflect.runtime.universe.reify

object C05 extends App {
  val someNumber = 123
  val numCherries = "numCherries" ->> someNumber // KeyTag[String("numCherries"),Int]
  println(reify(numCherries))
}
