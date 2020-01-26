package slick.x92slick

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp02d extends App {
  println( messages.result.statements.mkString)
  val action: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter({mt: MessageTable => mt.id > 2L})
  println( action.result.statements.mkString)

  // filter
  val action1a = action.filter(m => m.sender === "HAL")
  println(action1a.result.statements.mkString) // select "sender", "content", "id" from "message" where ("id" > 2) and ("sender" = 'HAL')

  // automatically exclude the clause from the WHERE statements if it is Option and equals None
  println("_____filterOpt")
  val name_some: Option[String] = Some("HAL")
  val name_none: Option[String] = None
  val action1b = action.filterOpt(name_some)( (row, value) => row.sender === value)
  println(action1b.result.statements.mkString) // select "sender", "content", "id" from "message" where ("id" > 2) and ("sender" = 'HAL')
  val action1c = action.filterOpt(name_none)( (row, value) => row.sender === value)
  val action1d = action.filterOpt(name_none)( _.sender === _) // first "_" is a row, second "_" is a value
  println(action1c.result.statements.mkString) // select "sender", "content", "id" from "message" where "id" > 2

  // automatically exclude the clause from the WHERE statements if flag == false
  println("_____filterIf")
  val flag1 = true
  val flag2 = false
  val action1e = action.filterIf(flag1)(_.sender === "HAL")
  val action1f = action.filterIf(flag2)(_.sender === "HAL")
  println(action1e.result.statements.mkString)
  println(action1f.result.statements.mkString)
  val action1g = action.distinct // select distinct "sender", "content", "id" from "message" where "id" > 2
  println(action1g.result.statements.mkString) //
  val action1h = action.length // select count(1) from "message" where "id" > 2
  println(action1h.result.statements.mkString)

  /**
   * map acts like a SELECT
   * filter is like a WHERE
   *
   * unpacked type, which is the regular Scala types we work with, such as String
   * mixed type, which is Slick’s column representation, such as Rep[String]
   */

}
