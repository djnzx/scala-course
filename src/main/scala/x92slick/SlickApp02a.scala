package x92slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object SlickApp02a extends App {
  val action: Query[MessageTable, MessageTable#TableElementType, Seq] = messages.filter({mt: MessageTable => mt.id > 3L})
  val dbio = action.result
  val future: Future[Seq[Message2]] = db.run(dbio)
  val resolved: Seq[Message2] = Await.result(future, 2 seconds)
  resolved foreach println
}
