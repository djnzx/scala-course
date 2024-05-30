package ntreads

object Reddit extends App {
  import scala.concurrent.{Future, Await}
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration.Duration

  def asyncCalculation1() = Future {
    pprint.log("1"->Thread.currentThread().getName)
    Thread.sleep(1000)
    10
  }

  def asyncCalculation2() = Future {
    pprint.log("2"->Thread.currentThread().getName)
    Thread.sleep(1000)
    20
  }

  val c = asyncCalculation1().flatMap { a =>
    pprint.log("fm"->Thread.currentThread().getName)
    asyncCalculation2().map { b =>
      pprint.log("m"->Thread.currentThread().getName)
      a + b
    }
  }

  val d = Await.result(c, Duration.Inf)
  pprint.log("main"->Thread.currentThread().getName)
  println(d)

  pprint.log(Runtime.getRuntime.availableProcessors)
}
