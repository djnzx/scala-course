package x93slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting many records
  * returning inserted records
  */
object SlickApp03e_insert_many_returning_records extends App {
  val db_i: DBIO[Seq[Country]] = countries.returning(countries) ++= Seq(
    Country("Croatia", "CR"),
  )
  Await.result(db.run(db_i), 2 seconds) foreach( println ) // Country(Croatia,CR,9)

  // OR
  val fullRow = countries.returning( countries.map(_.id)) into({ (c, id) => c.copy(id = id)})
  val insert: DBIO[Country] = fullRow += Country("Romania", "RO")
  val ro: Country = Await.result(db.run(insert), 2 seconds)
  println(ro)

}
