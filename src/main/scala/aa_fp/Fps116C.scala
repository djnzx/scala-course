package aa_fp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Fps116C extends App {

  val sl = (how: Long) => Thread.sleep(how)

  val t = () => System.currentTimeMillis()

  /**
    * these computations will start in parallel
    */
  val f1 = Future { val s = t(); println("F1 started"); sl(1000); val d = t()-s; println(s"F1 finished:$d"); d }
  val f2 = Future { val s = t(); println("F2 started"); sl(2000); val d = t()-s; println(s"F2 finished:$d"); d }
  val f3 = Future { val s = t(); println("F3 started"); sl(3000); val d = t()-s; println(s"F3 finished:$d"); d }

  val s0 = t()
  val r = for {
    fr1 <- f1
    fr2 <- f2
    fr3 <- f3
  } yield fr1 + fr2 + fr3
  r.onComplete {
    case Success(value) => println(s"done. value = ${value}, spent: ${t()-s0}")
    case Failure(ex)    => println("Exception was")
  }
  println("sleep...")
  Thread.sleep(10000)
  println(s"...woke up after ${t()-s0} ms")
}
