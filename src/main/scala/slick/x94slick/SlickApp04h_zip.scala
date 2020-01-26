package slick.x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp04h_zip extends App {
  val action1: DBIO[Seq[String]] = countries.sortBy(_.id).map(_.name).result
  val action2: DBIO[Seq[String]] = countries.sortBy(_.id).map(_.names).result

  val r1: Seq[String] = Await.result(db.run(action1), 2 second);
  val r2: Seq[String] = Await.result(db.run(action2), 2 second);

  val r3 = r1 zip r2

  println(r1)
  println(r2)
  println(r3)

  val action3: DBIO[(Seq[String], Seq[String])] = action1 zip action2
  val rz: (Seq[String], Seq[String]) = Await.result(db.run(action3), 2 second)
  println(rz)
}
