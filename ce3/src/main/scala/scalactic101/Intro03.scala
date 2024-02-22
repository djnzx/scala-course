package scalactic101

import org.scalactic._
import TypeCheckedTripleEquals._

object Intro03 extends App {

  val x = List(1, 2, 3)
    .map(Some(_))
    // compile time error
//    .filter(_ === 1)
    .filter(_ === Some(1))

}
