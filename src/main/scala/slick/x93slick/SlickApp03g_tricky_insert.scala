package slick.x93slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * inserting of no such record !!!
  *
  * insert into "messages" ("sender", "content") select 'Stanley', 'Cut!'
  * where
  * not exists(
  *    select
  *    "id", "sender", "content" from
  *    "messages" where "sender" = 'Stanley' and "content" = 'Cut!'
  * )
  */
object SlickApp03g_tricky_insert extends App {

  // data to insert
  val data: Query[
    (ConstColumn[String], ConstColumn[String]),
    (String, String),
    Seq
  ] =
    Query(("New Zeland", "NZ"))

  // exists statement
  val exists: Rep[Boolean] =
    countries.filter(c => c.name === "New Zeland" && c.names === "NZ").exists

  // select expression
  val selectExpression: Query[
    (ConstColumn[String], ConstColumn[String]),
    (String, String),
    Seq
  ] =
    data.filterNot(_ => exists)

  // final composition
  val action = countries
    .map(m => m.name -> m.names)
    .forceInsertQuery(selectExpression)

  Await.result(db.run(action), 2 second)
}

