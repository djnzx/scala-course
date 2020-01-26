package slick.x93slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting one record
  */
object SlickApp03b_insert_one extends App {
  val db_i = countries += Country("United States", "USA")
  println(countries.insertStatement)
  Await.result(db.run(db_i), 2 seconds)
}
