package slickvt


import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

trait SlickBase {

  val db: Database = Database.forURL(
    "jdbc:postgresql://localhost:5432/fs1",
    "postgres",
    "pg123456",
  )

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  implicit class QueryOps[TA, A, F[_]](q: Query[TA, A, F]) {
    def run = exec(q.result)

  }

}
