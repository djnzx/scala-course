package x95slick

import scala.concurrent.Await
import scala.concurrent.duration._

trait DatabaseModule { self: XProfile =>

  /**
    * here we import `val profile: JdbcProfile`
    * from our XProfile trait
    * part 5.1
    * page 100 book "Slick Essential"
    */
  import profile.api._
  import slick.lifted.{TableQuery, Tag}

  // current (new database)
  val db: Database = Database.forConfig("logistics")
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 10 seconds)

}
