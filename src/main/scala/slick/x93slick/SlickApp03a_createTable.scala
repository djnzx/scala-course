package slick.x93slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

object SlickApp03a_createTable extends App {
  val schema = countries.schema
  Await.result( db.run( DBIO.seq(
    schema.dropIfExists,
    schema.create
  )), 2 seconds)
}
