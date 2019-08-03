package x93slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting one record
  */
object SlickApp03f_insert_specific_fields extends App {
  val is = countries.map(_.name).insertStatement // insert into "country" ("name")  values (?)
  println(is)
  // The parameter type of the += method is matched to the unpacked type of the query
  val nz = countries.map(_.name) += "New Zeland!"
//  val db_i = countries += Country("United States", "USA")
//  println(countries.insertStatement)
  Await.result(db.run(nz), 2 seconds)
}

