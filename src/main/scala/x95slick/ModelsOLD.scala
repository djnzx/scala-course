package x95slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import x95slick.models.Models.{Country, Vendor}

object ModelsOLD {
  final class CountryTable_(tag: Tag) extends Table[Country](tag, "country") {
    def id      = column[Long]("co_id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("co_name");
    def names = column[String]("co_sname");
    def * = (name, names, id).mapTo[Country];
  }
  final class VendorTable_(tag: Tag) extends Table[Vendor](tag, "vendors") {
    def id      = column[Long]("v_id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("v_name");
    def * = (name, id).mapTo[Vendor];
  }

  lazy val countries0: TableQuery[CountryTable_] = TableQuery[CountryTable_]
  lazy val vendors0: TableQuery[VendorTable_] = TableQuery[VendorTable_]
}
