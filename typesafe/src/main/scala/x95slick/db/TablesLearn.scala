package slick.x95slick.db

import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape

trait TablesLearn { this: XProfile =>

  import profile.api._

  // ONE Message
  final case class Message(src: Long, dst: Long, text: String, flag: Option[MsgFlag] = None, id: Long = 0L)
  // Specific Field
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
  // Custom Mapping for Specific Field
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
  // MANY Messages (Table) with one specific field
  final class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def src = column[Long]("src")
    def dst = column[Long]("dst")
    def text = column[String]("text")
    def flag = column[Option[MsgFlag]]("flag", O.Default(Some(Normal)))
    // map top generic Message specified in declaration
    def * = (src, dst, text, flag, id).mapTo[Message]
  }
  // variable to make requests to MessageTable
  lazy val messages: TableQuery[MessageTable] = TableQuery[MessageTable]

  // ONE-column table
  final class MyTable(tag: Tag) extends Table[String](tag, "mytable10") {
    def column1 = column[String]("column1")
    def * = column1
  }

  // more than ONE-column table - ROW representation
  final case class Row2Col(name: String, id: Int)

  // way 1. case class: `Row2Col`
  // + mapping to case class
  final class MyTable21(tag: Tag) extends Table[Row2Col](tag, " mytable21") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2).mapTo[Row2Col]
  }

  // way 2. tuple: `(String, Int)`
  // + direct mapping
  final class MyTable22(tag: Tag) extends Table[(String, Int)](tag, " mytable22") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2)
  }

  // way3. case class: `Row2Col`
  // + custom mapping (<>) via `tupled` and `unapply`
  final class MyTable23(tag: Tag) extends Table[Row2Col](tag, " mytable23") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2) <> (Row2Col.tupled, Row2Col.unapply)
  }

  // way4. case class: `Row2Col`
  // + custom mapping (<>) via custom-written functions `intoRow2Col` and `fromRow2Col`
  final class MyTable24(tag: Tag) extends Table[Row2Col](tag, " mytable24") {
    def column1 = column[String]("column1")
    def column2 = column[Int]("column2")
    def * = (column1, column2) <> (intoRow2Col, fromRow2Col)
  }

  // tuple to Class function
  def intoRow2Col(pair: (String, Int)): Row2Col = Row2Col(pair._1, pair._2)
  // Class to Option[tuple] function
  def fromRow2Col(rc: Row2Col): Option[(String, Int)] = Some((rc.name, rc.id))

  // how to attach custom behavior (case class based)
  object mytable21 extends TableQuery( new MyTable21(_)) {
    // adding default queries to table
    def messagesById(id: Int): Query[MyTable21, Row2Col, Seq] = this.filter(_.column2 === id)
    val numSenders = this.map(_.column2).distinct.length
  }
  // how to attach custom behavior (tuple based)
  object mytable22 extends TableQuery( new MyTable22(_)) {
    def messagesById(id: Int): Query[MyTable22, (String, Int), Seq] = this.filter(_.column2 === id)
  }

  // 5.2.3 Heterogeneous Lists, You may have heard of HList via other libraries, such as shapeless
  // one row
  final case class WeirdRow(id: Long, attr1: String, val1: String, attr2: String, val2: String)
  // many rows (table)
  final class WeirdTable(tag: Tag) extends Table[WeirdRow](tag, "weird") {
    def id = column[Long]("id", O.AutoInc)
    def pk = primaryKey("pk_id", id)

    def attr1 = column[String]("attr1")
    def val1 = column[String]("val1")
    def attr2 = column[String]("attr2")
    def val2 = column[String]("val2")

    // general syntax: (some HList).mapTo[case class with the same fields]
    def * : ProvenShape[WeirdRow]  = (id :: attr1 :: val1 :: attr2 :: val2 :: HNil).mapTo[WeirdRow]
  }

  val weirdtable = TableQuery[WeirdTable]

  final case class BookShelfRow(topic: String, number: Int, id: Long = 0L)
  class BookShelf(tag: Tag) extends Table[BookShelfRow](tag, "books") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def topic = column[String]("topic")
    def number = column[Int]("number")
    def * = (topic, number, id).mapTo[BookShelfRow]
  }
  lazy val books = TableQuery[BookShelf]

}
