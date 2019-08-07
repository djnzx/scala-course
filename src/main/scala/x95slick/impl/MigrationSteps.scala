package x95slick.impl

import scala.concurrent.duration._
import x95slick.db.DatabaseLayer

final class MigrationSteps {
  private val persistence = new DatabaseLayer

  // import variables to local scope
  import persistence._             // in order to access to `val profile: JdbcProfile`, Tables definition and `exec` methods
  import persistence.profile.api._ // actually we import slick.jdbc.PostgresProfile.api._

  def move_countries: Unit = {
    // create table in the new db
    println(countries.schema.createStatements.mkString)             // ... ,"id" BIGSERIAL NOT NULL PRIMARY KEY
    println(countries.schema.createIfNotExistsStatements.mkString)  // ... ,"id" BIGINT NOT NULL PRIMARY KEY
    exec(countries.schema.create)
    // grab the data from old db
    val content: Seq[Country] = execa(countries0.sortBy(_.id).result)
    content foreach println
    // store the data to new db
    exec(countries ++= content)
  }

  def move_vendors: Unit = {
    exec(vendors.schema.create)
    val content: Seq[Vendor] = execa(vendors0.sortBy(_.id).result)
    content foreach println
    // read via `select last_value from vendor_id_seq`
    val correct_seq = sql"""select setval('vendor_id_seq'::regclass, MAX(id),true) FROM vendor""".as[Int]
    exec(vendors forceInsertAll content andThen correct_seq) // with old keys
  }

  def create_partnumbers: Unit = {
    exec(partnumbers.schema.create)
  }

}
