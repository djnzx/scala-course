package x91slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp01b extends App {
  // table row
  //                       PG.VARCHAR                       PG.BININT
  // https://alvinalexander.com/source-code/scala-how-create-case-class-multiple-alternate-constructors
  final case class Message2(sender: String, content: String, id: Long = 0)
//  object Message2 {
//    def apply(sender: String, content: String): Message2 = Message2(sender, content, 0)
//    def unapply(arg: Message2): Option[(String, String, Long)] = Option(arg.sender, arg.content, arg.id)
//  }

  // table schema
  final class MessageTable(tag: Tag) extends Table[Message2](tag, _tableName = "message") {
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc) // Unique, default value
    def sender  = column[String]("sender")
    def content = column[String]("content")
    // mapping function: tuple => ...
    def * = (sender, content, id).mapTo[Message2]
  }

  /**
    * Like Query, DBIOAction is also a monad.
    * It implements the same methods described above,
    * and shares the same compatibility with for comprehensions.
    */
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
  /**
    * ++= BulkInsert
    */
  val insert: DBIO[Option[Int]] = messages ++= freshTestData
//  val result_i: Future[Option[Int]] = db.run(insert)
//  val rowCount: Option[Int] = Await.result(result_i, 2.seconds)

  // Run the test query and print the results:
//  println("\nSelecting all messages:")
//  exec( messages.result ) foreach { println }

  /**
    * ++= One Line Insert
    */
  //Await.result( db.run(messages += Message2("Alex", "Alex, Smart")), 3 second)

  println("\nSelecting only messages from HAL:")
//  val messagesFiltered = messages.filter(_.sender.===("HAL"))
  /**
    * Query is a monad.
    * It implements the methods:
    * map, flatMap, filter, and withFilter,
    * making it compatible with Scala for comprehensions
    * see: slick book 1.4.9
    */
  val messagesFiltered = messages
    // ColumnExtensionMethods
    .filter(_.id > 0L)
    .filter(_.sender inSet Seq("HAL", "Alex"))
    // StringColumnExtensionMethods
    .filter(_.content like "%sorry%")

  println(messagesFiltered.result.statements.mkString)
  val messagesAction: DBIO[Seq[Message2]] = messagesFiltered.result
  val messagesFuture: Future[Seq[Message2]] = db.run(messagesAction)
  val messagesResults = Await.result(messagesFuture, 2 seconds)
//  messagesResults.foreach( println )
//  messagesResults.foreach({ println })
  messagesResults.foreach { println }

//  exec( filtered.result ) foreach { println }

  val sameActions: DBIO[Seq[Message2]] =
      messages.schema.create       >>
      (messages ++= freshTestData) >>
      messagesFiltered.result
}
