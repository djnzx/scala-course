package x098slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.duration._
import scala.concurrent.Await

object SlickApp02 extends App {

  // table row
  final case class Message2(
   sender:  String,
   content: String,    // PG.VARCHAR
   id:      Long = 0L) // PG.BININT

  // test data
  def freshTestData = Seq(
    Message2("Dave", "Hello, HAL. Do you read me, HAL?"),
    Message2("HAL",  "Affirmative, Dave. I read you."),
    Message2("Dave", "Open the pod bay doors, HAL."),
    Message2("HAL",  "I'm sorry, Dave. I'm afraid I can't do that.")
  )

  // schema
  final class MessageTable(tag: Tag) extends Table[Message2](tag, "message") {

    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def sender  = column[String]("sender")
    def content = column[String]("content")

    def * = (sender, content, id).mapTo[Message2]
  }

  // Base query for querying the messages table:
  lazy val messages = TableQuery[MessageTable]

  val halSays = messages.filter(_.sender === "HAL")

  val db: Database = Database.forConfig("chapter00")

  // Helper method for running a query in this example file:
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  // Create the "messages" table:
//  println("Creating database table")
//  exec(messages.schema.create)

  // Create and insert the test data:
//  println("\nInserting test data")
//  exec(messages ++= freshTestData)

  // Run the test query and print the results:
  println("\nSelecting all messages:")
  exec( messages.result ) foreach { println }

  println("\nSelecting only messages from HAL:")
  exec( halSays.result ) foreach { println }


}
