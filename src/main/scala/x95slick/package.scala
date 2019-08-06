import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

package object x95slick {
  // new db connection
  val db: Database = Database.forConfig("logistics")
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  // old db connection
  val dba: Database = Database.forConfig("avtomir")
  def execa[T](program: DBIO[T]): T = Await.result(dba.run(program), 2 seconds)
}
