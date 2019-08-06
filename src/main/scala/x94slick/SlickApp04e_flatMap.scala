package x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object SlickApp04e_flatMap extends App {
  val delete: DBIO[Int] = countries.filter(ct => ct.name endsWith("tia")).delete
  val delete2: slick.jdbc.PostgresProfile.ProfileAction[Int,slick.dbio.NoStream,slick.dbio.Effect.Write] =
    countries.filter(ct => ct.name endsWith("tia")).delete
  val insert = (count: Int) => countries += Country(s"country $count", s"CO $count")
//  val insert2 = (count: Int) => slick.sql.FixedSqlAction[Int,slick.dbio.NoStream,slick.dbio.Effect.Write] =
  val combined1 = delete.flatMap(count => if (count>0) insert(count) else DBIO.successful(-1))
  val combined2 = delete.flatMap(count => count match {
    case 0 => DBIO.successful(-1)
    case n => insert(n)
  })
  val combined3 = delete.flatMap {
    case 0 => DBIO.successful(-1)
    case n => insert(n)
  }
//  val inserted: Int = Await.result(db.run(combined), 2 seconds)
//  println(inserted)
}
