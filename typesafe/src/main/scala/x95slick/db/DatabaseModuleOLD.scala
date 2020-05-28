package slick.x95slick.db

import scala.concurrent.Await

trait DatabaseModuleOLD { this: XProfile with XTimeout =>

  import profile.api.{ Database, DBIO }

  val dba: Database = Database.forConfig("avtomir")
  def execa[T](program: DBIO[T]): T = Await.result(dba.run(program), timeout)

}
