package slick.x95slick.db

trait Tables { this: XProfile => // this means this trait should be mixed with XProfile

  import profile.api._

  // ONE Country
  final case class Country(name: String, names: String, id: Long = 0L)
  // ONE Vendor
  final case class Vendor(name: String, id: Long = 0L)
  // MANY Countries
  final class CountryTable(tag: Tag) extends Table[Country](tag, "country") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("name");
    def names = column[String]("names");
    def * = (name, names, id).mapTo[Country];
  }
  // MANY Vendors
  final class VendorTable(tag: Tag) extends Table[Vendor](tag, "vendor") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("name");
//    def * = (name, id).mapTo[Vendor];
    def * = (name, id).mapTo[Vendor];
  }
  // MANY Countries - OLD
  final class CountryTable_(tag: Tag) extends Table[Country](tag, "country") {
    def id      = column[Long]("co_id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("co_name");
    def names = column[String]("co_sname");
    def * = (name, names, id).mapTo[Country];
  }
  // MANY Vendors - OLD
  final class VendorTable_(tag: Tag) extends Table[Vendor](tag, "vendors") {
    def id      = column[Long]("v_id", O.PrimaryKey, O.AutoInc);
    def name  = column[String]("v_name");
    def * = (name, id).mapTo[Vendor];
  }
  // ONE PartNumber
  case class PartNumber(vendor: Long, number: String, id: Long = 0)
  // MANY PartNumbers
//  final class PartNumbers(tag: Tag) extends Table[(Long, Int, String)](tag, "part_number") {
  final class PartNumbers(tag: Tag) extends Table[PartNumber](tag, "part_number") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def vendor_id = column[Long]("vendor")
    def number = column[String]("number")
    def * = (vendor_id, number, id).mapTo[PartNumber]
//    def * = (id, vendor, number)
    def vendor =
      foreignKey("fk_vendor", vendor_id, vendors)(_.id)
  }

  // variables/objects to make requests
  lazy val countries: TableQuery[CountryTable] = TableQuery[CountryTable]
  lazy val vendors: TableQuery[VendorTable] = TableQuery[VendorTable]
  object countries0 extends TableQuery(new CountryTable_(_))
  object vendors0 extends TableQuery(new VendorTable_(_))
  lazy val partnumbers: TableQuery[PartNumbers] = TableQuery[PartNumbers]

}
