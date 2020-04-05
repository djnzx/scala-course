package cats

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.list._

object C172Traversable extends App {

  val hosts = List(
    "alpha.ibm.com",
    "beta.ibm.com",
    "lambda.ibm.com",
  )

  val getUptime: String => Future[Int]       = name => Future { name.length*10 }
  val allUptime:           List[Future[Int]] = hosts.map(name => getUptime(name))

  /**
    * we need to convert
    * `List[Future[Int]]` => Future[List[Int]
    * the main idea to foldLeft
    * and `Future[List.empty[Int]]` use as empty element
    * and after that use `flatMap` to combine it
    */
  val zero:    Future[List[Int]] = Future { List.empty[Int] }

  val converted1: Future[List[Int]] = allUptime
    .foldLeft(zero)((flist: Future[List[Int]], fint: Future[Int]) =>
    for {
      list <- flist
      int  <- fint
    } yield list :+ int
  )
  val r1: List[Int] = Await.result(converted1, 1.seconds)

  val converted2: Future[List[Int]] = Foldable[List].
    foldLeft(hosts, zero)((flist: Future[List[Int]], host: String) =>
    for {
      list <- flist           // Future[List[Int]]
      int <- getUptime(host) // Future[Int]
    } yield list :+ int      // Future[List[Int] :+ Int]
  )
  val r2: List[Int] = Await.result(converted2, 10.seconds)

  val converted3: Future[List[Int]] = Future.traverse(hosts)(h => getUptime(h))
  val r3: List[Int] = Await.result(converted3, 10.seconds)

  println(converted1)
  println(r1)
  println(converted2)
  println(r2)
  println(converted3)
  println(r3)

}
