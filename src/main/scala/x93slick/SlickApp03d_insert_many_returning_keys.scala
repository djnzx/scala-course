package x93slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting many records
  * returning keys of inserted records
  */
object SlickApp03d_insert_many_returning_keys extends App {
  val db_i: DBIO[Seq[Long]] = countries.returning(countries.map(_.id)) ++= Seq(
    Country("Bulgaria", "BG"),
    Country("Austria", "AU"),
  )
  Await.result(db.run(db_i), 2 seconds) foreach( println ) // [7, 8]
}
