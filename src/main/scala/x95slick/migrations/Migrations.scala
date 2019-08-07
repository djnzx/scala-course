package x95slick.migrations

import slick.jdbc.PostgresProfile.api._
import x95slick.models.Models.{Country, Vendor, countries, vendors}
import x95slick.ModelsOLD.{countries0, vendors0}
import x95slick.{DatabaseLayer, XProfile}

object Migrations {

  val persistence = new DatabaseLayer(slick.jdbc.PostgresProfile)

  val move_countries: () => Unit = () => {
    // create table in the new db
    println(countries.schema.createStatements.mkString)             // ... ,"id" BIGSERIAL NOT NULL PRIMARY KEY
    println(countries.schema.createIfNotExistsStatements.mkString)  // ... ,"id" BIGINT NOT NULL PRIMARY KEY
    persistence.exec(countries.schema.create)
    // grab the data from old db
    val content: Seq[Country] = persistence.execa(countries0.sortBy(_.id).result)
    content foreach println
    // store the data to new db
    persistence.exec(countries ++= content)
  }

  val move_vendors: () => Unit = () => {
    persistence.exec(vendors.schema.create)
    val content: Seq[Vendor] = persistence.execa(vendors0.sortBy(_.id).result)
    content foreach println
    // read via `select last_value from vendor_id_seq`
    val correct_seq = sql"""select setval('vendor_id_seq'::regclass, MAX(id),true) FROM vendor""".as[Int]
    persistence.exec(vendors forceInsertAll content andThen correct_seq) // with old keys
  }

}
