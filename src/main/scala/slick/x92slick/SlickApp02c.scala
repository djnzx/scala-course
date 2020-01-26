package slick.x92slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp02c extends App {

  println( messages.result.statements.mkString)

  val action: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter({mt: MessageTable => mt.id > 2L})
  println( action.result.statements.mkString)
  // sortBy
  val action1a = action.sortBy(_.sender)
  println(action1a.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender"
  val action1b = action.sortBy(_.sender.desc)
  println(action1b.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" desc
  val action1c = action.sortBy(el => (el.sender.desc, el.content.asc))
  println(action1c.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" desc, "content"
  val action1d = action.sortBy(_.sender.nullsFirst)
  println(action1d.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" nulls first
  val action1e = action.sortBy(_.sender.nullsLast)
  println(action1e.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender" nulls last
  val action1f = action.sortBy(_.sender.nullsDefault)
  println(action1f.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 order by "sender"

  // take
  val action2a = action.take(2)
  println(action2a.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 limit 2

  // drop
  val action3a = action.drop(2)
  println(action3a.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 offset 2

  // drop + take
  val action3b = action.drop(2).take(2)
  println(action3b.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2 limit 2 offset 2

//  val e = Await.result(db.run(action4.exists.result), 10 second)
}
