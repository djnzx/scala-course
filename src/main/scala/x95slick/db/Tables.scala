package x95slick.db

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
  case class PartNumber(id: Long, vendor: Int, number: String)
  // MANY PartNumbers
  final class PartNumbers(tag: Tag) extends Table[(Long, Int, String)](tag, "part_number") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def vendor = column[Int]("vendor")
    def number = column[String]("number")
    def * = (id, vendor, number)
  }

  // variables/objects to make requests
  lazy val countries: TableQuery[CountryTable] = TableQuery[CountryTable]
  lazy val vendors: TableQuery[VendorTable] = TableQuery[VendorTable]
  object countries0 extends TableQuery(new CountryTable_(_))
  object vendors0 extends TableQuery(new VendorTable_(_))
  lazy val partnumbers: TableQuery[PartNumbers] = TableQuery[PartNumbers]

  final case class Message(src: Long, dst: Long, text: String, flag: Option[MsgFlag] = None, id: Long = 0L)

  sealed trait MsgFlag
  case object Important extends MsgFlag
  case object Offensive extends MsgFlag
  case object Spam extends MsgFlag
  case object Normal extends MsgFlag
  object Flags {
    val important : MsgFlag = Important
    val offensive : MsgFlag = Offensive
    val spam : MsgFlag = Spam
    val normal : MsgFlag = Normal
  }

  implicit val flagType = MappedColumnType.base[MsgFlag, Char](
    fllag => fllag match {
      case Important => '!'
      case Offensive => 'X'
      case Spam => '$'
      case Normal => ' '
    },
    code => code match {
      case '!' => Important
      case 'X' | 'x' => Offensive
      case '$' => Spam
      case _ => Normal
    }
  )

  final class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def src = column[Long]("src")
    def dst = column[Long]("dst")
    def text = column[String]("text")
    def flag = column[Option[MsgFlag]]("flag", O.Default(Some(Normal)))
    def * = (src, dst, text, flag, id).mapTo[Message]
  }
  lazy val messages: TableQuery[MessageTable] = TableQuery[MessageTable]

}
