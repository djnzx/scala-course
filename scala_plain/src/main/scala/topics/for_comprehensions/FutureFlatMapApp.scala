package topics.for_comprehensions

import java.util.concurrent.CountDownLatch

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Failure, Success}

object FutureFlatMapApp extends App {
//  scala.concurrent.ExecutionContext.fromExecutor()
  val cdl = new CountDownLatch(1)
  (for {
    f1 <- Future {
      Thread.sleep(1000)
      1
    }
    f2 <- Future {
      Thread.sleep(1000)
      2
    }
    f3 <- Future {
      Thread.sleep(1000)
      3
    }
  } yield f1 + f2 + f3).onComplete { x =>
    cdl.countDown()
    x match {
      case Success(value) => println(value)
      case Failure(exception) => exception.printStackTrace()
    }
  }
  cdl.await()
//  println(Runtime.getRuntime.availableProcessors()) // 8
}
