package slick.x95slick.db

import scala.concurrent.Await

trait DatabaseModule { this: XProfile with XTimeout =>

  import profile.api.{ Database, DBIO }

  val db: Database = Database.forConfig("logistics")
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), timeout)

}
