package x098slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object SlickApp02c extends App {

  println( messages.result.statements.mkString)

  val action: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter({mt: MessageTable => mt.id > 2L})
  println( action.result.statements.mkString)
  val action1a = action.sortBy(_.sender)
  println(action1a.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender"
  val action1b = action.sortBy(_.sender.desc)
  println(action1b.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" desc
  val action1c = action.sortBy(el => (el.sender.desc, el.content.asc))
  println(action1c.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" desc, "content"

  val action2: Query[Rep[String], String, Seq] = action.map(_.content)
  val action4 = action.map(x => (x.id, x.sender ++ ">" ++ x.content toLowerCase))

  val dbio: DBIO[Seq[String]] = action2.result
  val future: Future[Seq[String]] = db.run(dbio)
  val resolved: Seq[String] = Await.result(future, 2 seconds)
  resolved foreach println

  val dbio_ = action4.result
  val future_ = db.run(dbio_)
  Await.result(future_, 10 second) foreach println

  val e = Await.result(db.run(action4.exists.result), 10 second)
  println(e)
}
