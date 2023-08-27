package slickvt


import akka.stream.scaladsl.Source
import slick.dbio.{DBIOAction, Streaming}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

trait SlickBase {

  val db: Database = Database.forURL(
    "jdbc:postgresql://localhost:5432/fs1",
    "postgres",
    "pg123456",
  )

  private def exec0[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)
  private def stream0[T](program: DBIOAction[_, Streaming[T], Nothing]) = Source.fromPublisher(db.stream(program))

  implicit class QueryOps[TA, A, F[_]](q: Query[TA, A, F]) {
    def run = exec0(q.result)
    def stream = stream0(q.result)
  }

}
