package aa_fp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Fps116B extends App {

  val sl = (how: Long) => Thread.sleep(how)

  val t = () => System.currentTimeMillis()

  val s0 = t()

  /**
    * these computations will start in sequential order. one after another
    */
  val r: Future[Long] = for {
    fr1 <- Future { val s = t(); println("F1 started"); sl(1000); val d = t()-s; println(s"F1 finished:$d"); d }
    fr2 <- Future { val s = t(); println("F2 started"); sl(2000); val d = t()-s; println(s"F2 finished:$d"); d }
    fr3 <- Future { val s = t(); println("F3 started"); sl(3000); val d = t()-s; println(s"F3 finished:$d"); d }
  } yield fr1 + fr2 + fr3
  r.onComplete {
    case Success(value) => println(s"done. value = ${value}, spent: ${t()-s0}")
    case Failure(_)     => println("Exception was")
  }
  println("sleep...")
  Thread.sleep(10000)
  println(s"...woke up after ${t()-s0} ms")
}
