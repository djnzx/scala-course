package slick.x93slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting many records
  */
object SlickApp03c_insert_many extends App {
  val db_i = countries ++= Seq(
    Country("Ukraine", "UA"),
    Country("United Arab Emirates", "UAE"),
  )
  println( db_i.statements.mkString)
  Await.result(db.run(db_i), 2 seconds) // Option[ how many records is inserted]
}
