package x95slick

import scala.concurrent.Await
import scala.concurrent.duration._

trait DatabaseModuleOLD { self: XProfile =>

  import profile.api._

  // old db connection
  val dba: Database = Database.forConfig("avtomir")
  def execa[T](program: DBIO[T]): T = Await.result(dba.run(program), 10 seconds)

}
