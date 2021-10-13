package d01

import cats.implicits.catsSyntaxTuple2Semigroupal

import scala.concurrent.duration.DurationInt
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object FutureComposition extends App {

  implicit val ec = ExecutionContext.global
  def hello = Future(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] Hello"))
  def world = Future(println(s"[${ Thread.sleep(2000); Thread.currentThread.getName }] World"))

  /** sequential, can depend on previous */
  val hw1: Future[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()

  Await.ready(hw1, 5.seconds)

  /** parallel, (actually concurrent!), can't depend on other */
  val hw2: Future[Unit] =
    (hello, world)
      .mapN((_, _) => ())

  Await.ready(hw2, 5.seconds)

}
