package x95slick.models

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._

object Models {
  final case class Country(name: String, names: String, id: Long = 0L)
  final case class Vendor(name: String, id: Long = 0L)

  final class CountryTable(tag: Tag) extends Table[Country](tag, "country") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("name");
    def names = column[String]("names");
    def * = (name, names, id).mapTo[Country];
  }

  final class VendorTable(tag: Tag) extends Table[Vendor](tag, "vendor") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("name");
    def * = (name, id).mapTo[Vendor];
  }

  lazy val countries: TableQuery[CountryTable] = TableQuery[CountryTable]
  lazy val vendors: TableQuery[VendorTable] = TableQuery[VendorTable]
}
