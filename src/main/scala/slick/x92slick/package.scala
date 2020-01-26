package slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

package object x92slick {
  final case class Message2(sender: String, content: String, id: Long = 0)
  final case class Message20(sender: String, content: Option[String], id: Long = 0)

  final class MessageTable(tag: Tag) extends Table[Message2](tag, _tableName = "message") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc) // Unique, default value
    def sender  = column[String]("sender")
    def content = column[String]("content")
    // mapping function: tuple => ...
    def * = (sender, content, id).mapTo[Message2]
  }
  final class MessageTable0(tag: Tag) extends Table[Message20](tag, _tableName = "message") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc) // Unique, default value
    def sender  = column[String]("sender")
    def content = column[Option[String]]("content")
    // mapping function: tuple => ...
    def * = (sender, content, id).mapTo[Message20]
  }
  val db: Database = Database.forConfig("chapter01my")

  // Helper method for running a query in this example file:
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  // Base query for querying the messages table: definition only
  lazy val messages: TableQuery[MessageTable] = TableQuery[MessageTable]
  lazy val messagesO: TableQuery[MessageTable0] = TableQuery[MessageTable0]

}
