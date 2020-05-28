package x94slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object SlickApp04g_fold extends App {
  val report1: DBIO[Int] = DBIO.successful(41)
  val report2: DBIO[Int] = DBIO.successful(1)
  val reports_list: List[DBIO[Int]] = report1 :: report2 :: Nil
  val initial: Int = 0

  val reports_sum: DBIO[Int] = DBIO.fold(reports_list, initial) {
    (total, curr) => total + curr }

  val total: Int = Await.result(db.run(reports_sum), 5 seconds)
  println(total)
}
