package aa_fp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Fps116D extends App {

  val sl = (how: Long) => Thread.sleep(how)
  val t = () => System.currentTimeMillis()
  val dt: Long => Long = (t0: Long) => t() - t0
  val tn = () => Thread.currentThread().getId

  /**
    * these computations will start in parallel
    */
  val f1 = Future { val s = t(); println(s"F1 started:${tn()}"); sl(1000); println(s"F1 finished:${dt(s)}"); 10 }
  val f2 = Future { val s = t(); println(s"F2 started:${tn()}"); sl(2000); println(s"F2 finished:${dt(s)}"); 20 }
  val f3 = Future { val s = t(); println(s"F3 started:${tn()}"); sl(3000); println(s"F3 finished:${dt(s)}"); 30 }

  val s0 = t()
  val r = for {
    fr1 <- f1
    fr2 <- f2
    fr3 <- f3
  } yield fr1 + fr2 + fr3
  r.onComplete {
    case Success(value) => println(s"done. value = ${value}, spent: ${t()-s0}, thread:${tn()}")
    case Failure(ex)    => println("Exception was")
  }

  println("sleep...")
  Thread.sleep(10000)
  println(s"...woke up after ${t()-s0} ms, main thread id: ${tn()}")
}
