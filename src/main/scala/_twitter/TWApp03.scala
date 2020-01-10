package _twitter

import scala.language.implicitConversions

/**
  * Scala Advanced Types
  * https://twitter.github.io/scala_school/advanced-types.html
  */
object TWApp03 extends App {
  implicit def strToInt(x: String) = x.toInt
  val max: Int = math.max("123", 111)

  /**
    * View bounds: deprecated
    * `A` has to be `viewable` as Int
    */
  class Container[A <% Int] { def addIt(x: A): Int = 123 + x }
  (new Container[String]).addIt("123")
  (new Container[Int]).addIt(123)
  // could not find implicit value for evidence parameter of type (Float) => Int
//  (new Container[Float]).addIt(123.2F)

  /**
    * Other type bounds
    */

}
