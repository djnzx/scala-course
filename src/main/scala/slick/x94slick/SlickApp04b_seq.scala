package slick.x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object SlickApp04b_seq extends App {
  val mapper = (i: Int) => Country(s"Country $i", s"CO $i")

  val range1 = Range.inclusive(21, 30)
  val range2 = Range.inclusive(31, 40)

  val action1 = countries ++= range1.map(mapper)
  val action2 = countries ++= range2.map(mapper)

  /**
    * run in sequence, but discard everything result
    */
  val action3 = DBIO.seq(
    action1,
    action2
  )
  Await.result(db.run(action3), 2 seconds)
}
