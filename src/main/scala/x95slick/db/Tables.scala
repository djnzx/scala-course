package x95slick.db

import slick.lifted.ProvenShape
import slick.collection.heterogeneous.{ HCons, HList, HNil } // object
import slick.collection.heterogeneous.syntax._

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
    def * : ProvenShape[Vendor] = (name, id).mapTo[Vendor];
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
    def vendor = column[Long]("vendor")
    def number = column[String]("number")
    def * = (vendor, number, id).mapTo[PartNumber]
//    def * = (id, vendor, number)
    def fk_vendor =
      foreignKey("fk_vendor", vendor, vendors)(_.id)
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
    // map top generic Message specified in declaration
    def * = (src, dst, text, flag, id).mapTo[Message]
  }
  lazy val messages: TableQuery[MessageTable] = TableQuery[MessageTable]

  type HList0 = HNil
  type HList1 = Int :: HNil
  type HList2 = Int :: String :: Boolean :: HNil

  val emptyHList1 = HNil
  val emptyHList2: HList0 = HNil
  val emptyHList3: HNil = HNil

  val shortHList1 = 123 :: HNil
  val shortHList2: HList1 = 123 :: HNil
  val shortHList3: Int :: HNil = 123 :: HNil

  val longerHList1
  = 123 :: "abc" :: true :: HNil
  val longerHList2: HList2
  = 123 :: "abc" :: true :: HNil
  val longerHList3: Int :: String :: Boolean :: HNil
  = 123 :: "abc" :: true :: HNil

  // 5.2.3 Heterogeneous Lists, You may have heard of HList via other libraries, such as shapeless
  //  def * = (some hlist).mapTo[case class with the same fields]
  final case class WeirdRow(id: Long, attr1: String, val1: String, attr2: String, val2: String)

  final class WeirdTable(tag: Tag) extends Table[WeirdRow](tag, "weird") {
    def id = column[Long]("id", O.AutoInc)
    def pk = primaryKey("pk_id", id)

    def attr1 = column[String]("attr1")
    def val1 = column[String]("val1")
    def attr2 = column[String]("attr2")
    def val2 = column[String]("val2")

    // way 1
//    def * : ProvenShape[WeirdRow] = (id, attr1, val1, attr2, val2).mapTo[WeirdRow]
    // way 2
    def * : ProvenShape[WeirdRow]  = (id :: attr1 :: val1 :: attr2 :: val2 :: HNil).mapTo[WeirdRow]
  }

  val weirdtable = TableQuery[WeirdTable]
  // one-column table
  final class MyTable(tag: Tag) extends Table[String](tag, "mytable") {
    def column1 = column[String]("column1")
    def * = column1
  }
  // more than one-column table
  final case class Row2Col(name: String, id: Int)

  // way 1. case class + case class
  final class MyTable20(tag: Tag) extends Table[Row2Col](tag, " mytable21") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2).mapTo[Row2Col]
  }
  // way 2. tuple
  final class MyTable21(tag: Tag) extends Table[(String, Int)](tag, " mytable22") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2)
  }
  object mytable21 extends TableQuery( new MyTable21(_)) {
    // adding default queries tu
    def messagesById(id: Int): Query[MyTable21, (String, Int), Seq] = this.filter(_.column2 === id)
    val numSenders = this.map(_.column2).distinct.length
  }
  // way3. case class + <>
  final class MyTable22(tag: Tag) extends Table[Row2Col](tag, " mytable23") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2) <> (Row2Col.tupled, Row2Col.unapply)
  }
  // way4. case class + <> + custom mapper / unmapper
  def intoRow2Col(pair: (String, Int)): Row2Col = Row2Col(pair._1, pair._2)
  def fromRow2Col(rc: Row2Col): Option[(String, Int)] = Some((rc.name, rc.id))
  final class MyTable23(tag: Tag) extends Table[Row2Col](tag, " mytable24") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2) <> (intoRow2Col, fromRow2Col)
  }

}
