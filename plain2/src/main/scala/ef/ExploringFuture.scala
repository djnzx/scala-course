package ef

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object ExploringFuture extends App {

  val f1: Future[Int] = Future {
    println("1st stage")
    1
  }

  val f2 = f1.map { _ =>
    println("2nd stage")
    2.2
  }

  f1.onComplete(_ => println("Done 1"))
  f2.onComplete(_ => println("Done 2"))

  val r = Await.result(f1, 10.seconds)

  println(r)

}
