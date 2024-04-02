package cats101

import cats.{Apply, Semigroupal, Traverse}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.instances.list._
import cats.instances.future._
import cats.syntax.apply._

object C172TraversableRepeat1 extends App {

  val hosts = List(
    "alpha.ibm.com",
    "beta.ibm.com",
    "gamma.ixbt.com"
  )

  val calcUptime: String => Future[Int] = (host: String) => Future { host.length * 100 }
  val result0: List[Future[Int]] = hosts.map(calcUptime)

  /**
    * the task is to
    * get Future[List[Int]],
    * not List[Future[Int]]
    */
  val combineFn1: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
    (acc, uptime) => acc.flatMap(list => uptime.map(time => list :+ time))

  val combineFn2: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
    (acc: Future[List[Int]], uptime: Future[Int]) => for {
      list <- acc
      time <- uptime
    } yield list :+ time

  val combineFn3: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
    (acc, uptime) => Semigroupal.map2(acc, uptime)(_ :+ _)

  val combineFn4: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
    (acc, uptime) => (acc, uptime).mapN(_ :+ _)

  val combineFn5: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
    (acc, uptime) => {
      val r: Future[(List[Int], Int)] = Apply[Future].product(acc, uptime)
      r.map { t: (List[Int], Int) => t._1 :+ t._2 }
    }

  val result1: Future[List[Int]] = result0.foldLeft(Future { List.empty[Int] })(combineFn2)
  val result2: Future[List[Int]] = Future.        traverse(hosts)(calcUptime) // specific
  val result3: Future[List[Int]] = Traverse[List].traverse(hosts)(calcUptime) // generic

  val result_extracted: List[Int] = Await.result(result3, 1.seconds)
  println(result_extracted)

  // or via Applicative


}
