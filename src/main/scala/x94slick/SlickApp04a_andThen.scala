package x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object SlickApp04a_andThen extends App {
  val mapper = (i: Int) => Country(s"Country $i", s"CO $i")

  val range1 = Range.inclusive(21, 30)
  val range2 = Range.inclusive(31, 40)

  val action1 = countries ++= range1.map(mapper)
  val action2 = countries ++= range2.map(mapper)

  /**
    * runs action2 after action1
    * if either failed returns failed
    * else returns result of action2
    */
  //
  val action3 = action1 andThen action2
//  val action3 = action1 >> action2

  val number: Option[Int] = Await.result(db.run(action3), 2 seconds) // Option[ how many records is inserted]
  println(number)
}
