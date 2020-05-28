package x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object SlickApp04d_success_failed extends App {
  /**
    * particular values
    */
  val v100: DBIO[Int] = DBIO.successful(100)
  val vFail: DBIO[Nothing] = DBIO.failed(new RuntimeException("went wrong"))

  // ignoring 2-nd (failed)
  val combined1 = v100 andFinally vFail
//  val r1: Int = Await.result(db.run(combined1), 2 seconds)
//  println(r1)

//  val combined2 = v100 andThen vFail
//  val future2 = db.run(combined2)
//  val r2: Nothing = Await.result(future2, 2 seconds)
//  println(r2)
}
