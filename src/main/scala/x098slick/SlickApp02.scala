package x098slick

import slick.lifted.{TableQuery, Tag}

// Library Code
import slick.jdbc.PostgresProfile.api._
// async
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.Await

object SlickApp02 extends App {
  // table row
  //                        PG.VARCHAR                          PG.BININT
  final case class Message2(sender: String, content: String, id: Long = 0L)

  // table schema
  final class MessageTable(tag: Tag) extends Table[Message2](tag, _tableName = "message") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc) // Unique, default value
    def sender  = column[String]("sender")
    def content = column[String]("content")
    // mapping function: tuple => ...
    def * = (sender, content, id).mapTo[Message2]
  }

  // Helper method for running a query in this example file:
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  // Base query for querying the messages table: definition only
  lazy val messages: TableQuery[MessageTable] = TableQuery[MessageTable]

  // definition only
  lazy val filtered: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter(_.sender === "HAL")

  // connection
  // src/main/resources/application.conf
  val db: Database = Database.forConfig("chapter01my")

  // test data
  def freshTestData: Seq[Message2] = Seq(
    Message2("Dave", "Hello, HAL. Do you read me, HAL?"),
    Message2("HAL",  "Affirmative, Dave. I read you."),
    Message2("Dave", "Open the pod bay doors, HAL."),
    Message2("HAL",  "I'm sorry, Dave. I'm afraid I can't do that.")
  )

  // Create the "messages" table:
  println("SQL to create table")
  println(messages.schema.createStatements.mkString)
//  val action: DBIO[Unit] = messages.schema.create
  // async run
//  val future: Future[Unit] = db.run(action)
  // resolve future with 2 sec timeout
//  val result = Await.result(future, 2.seconds)


  //  println("Creating database table")
//  exec(messages.schema.create)

  // Create and insert the test data:
//  println("\nInserting test data")
  val insert: DBIO[Option[Int]] = messages ++= freshTestData
//  val result_i: Future[Option[Int]] = db.run(insert)
//  val rowCount: Option[Int] = Await.result(result_i, 2.seconds)

  // Run the test query and print the results:
//  println("\nSelecting all messages:")
//  exec( messages.result ) foreach { println }

  println("\nSelecting only messages from HAL:")
//  val messagesFiltered = messages.filter(_.sender.===("HAL"))
  val messagesFiltered = messages
    .filter(_.sender === "HAL")
    .filter(_.id > 2L)

  println(messagesFiltered.result.statements.mkString)
  val messagesAction: DBIO[Seq[Message2]] = messagesFiltered.result
  val messagesFuture: Future[Seq[Message2]] = db.run(messagesAction)
  val messagesResults = Await.result(messagesFuture, 2 seconds)
//  messagesResults.foreach( println )
//  messagesResults.foreach({ println })
  messagesResults.foreach { println }

//  exec( filtered.result ) foreach { println }


}
