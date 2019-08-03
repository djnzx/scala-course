package x92slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp02b extends App {

//  case class TextOnly(id: Long, content: String)

  println( messages.result.statements.mkString)

  val action: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter({mt: MessageTable => mt.id > 2L})
  println( action.result.statements.mkString)

  val action2: Query[Rep[String], String, Seq] = action.map(_.content)
  val action3: Query[(Rep[String], Rep[String]), (String, String), Seq] = action.map(x => (x.content, x.sender))
//  val action4: Query[(Rep[Long], Rep[String], Rep[String]), (Long, String, String), Seq] = action.map(x => (x.id * 1000L, x.content, x.sender))
//  val action4: Query[TextOnly, (Long, String), Seq] = action.map(x => (x.id, x.sender).mapTo[TextOnly])
  val action4 = action.map(x => (x.id, x.sender ++ ">" ++ x.content toLowerCase))
  println( action2.result.statements.mkString)
  println( action3.result.statements.mkString)
  println( action4.result.statements.mkString)
  println( action4.exists.result.statements.mkString)

  val dbio: DBIO[Seq[String]] = action2.result
  val future: Future[Seq[String]] = db.run(dbio)
  val resolved: Seq[String] = Await.result(future, 2 seconds)
  resolved foreach println

  val dbio_ = action4.result
  val future_ = db.run(dbio_)
  Await.result(future_, 10 second) foreach println

  val e = Await.result(db.run(action4.exists.result), 10 second)
  println(e)
//  val ex = Await.result(db.run(messagesO.result), 10 second)
//  println(ex)
}
