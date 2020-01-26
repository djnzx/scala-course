package twitter

import java.util.concurrent.{Callable, ExecutorService, Executors, FutureTask, TimeUnit}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object TWApp11Future extends App {

  // java FutureTask is a RunnableFuture is Runnable, Future
  val future: FutureTask[String] = new FutureTask[String](new Callable[String] {
    override def call(): String = {
      "abc"
    }
  })

  def bulkGet(keys: List[String]): Seq[String] = {
    val listFut: Seq[Future[String]] = keys.map(_ => Future("network get request here"))
    val values = Future.sequence(listFut)
    Await.result(values, Duration(2, TimeUnit.SECONDS))
  }

  def oneGet(key: String): String = {
    val fut: Future[String] = Future { key }
    Await.result(fut, Duration(2, TimeUnit.SECONDS))
  }

  val pool: ExecutorService = Executors.newFixedThreadPool(3)

  pool.execute(future)

//  Await.result(future, Duration(2, TimeUnit.SECONDS))
//  val blockingResult = Await.result(future, 2000)

  pool.shutdown()

}
