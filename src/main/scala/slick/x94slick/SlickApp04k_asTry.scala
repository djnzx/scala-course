package slick.x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object SlickApp04k_asTry extends App {
  val work1x = DBIO.failed(new RuntimeException("Boom!"))
  val work2x = DBIO.successful(91)
  List(work1x, work2x)
    .foreach(item => {
      val rx = Await.result(db.run(item.asTry), 2 second)
      println(rx match {
        case Success(value: Int) => s"SUCCESS:INT: $value"
        case Failure(throwable: Throwable) => s"FAILURE: $throwable"
      })
    })
}
