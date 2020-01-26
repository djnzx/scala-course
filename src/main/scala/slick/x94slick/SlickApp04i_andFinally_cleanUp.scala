package slick.x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object SlickApp04i_andFinally_cleanUp extends App {
  /**
    * The two methods cleanUp and andFinally
    * act a li􏰁le like Scala’s catch and finally.
    */
  def log(err: Throwable): DBIO[Int] = countries += Country("SYSTEM", err.getMessage)
  val log1: Throwable => DBIO[Int] = err => countries += Country("SYSTEM", err.getMessage)
  /**
    * let it be some important work
    */
//  val work = DBIO.failed(new RuntimeException("Boom!"))
  val work = DBIO.successful(91)

  /**
    * Both cleanUp() and andFinally() run aft􏰂er an action, regardless of whether it succeeds or fails.
    *
    * cleanUp() runs in response to a previous failed action;
    *
    * andFinally() runs all the time, regardless of success or failure,
    * and has no access to the Option[Throwable] that cleanUp sees.
    */
  val action: DBIO[Int] = work.cleanUp((t: Option[Throwable]) => t match {
    case Some(err) => log(err) andThen DBIO.successful(-13)
    case None => DBIO.successful(0)
  })

  /**
    * it still crushes but produces side effect by cleanUp
    */
  val r: Int = Await.result(db.run(action), 2 second)
  println(r)
}
