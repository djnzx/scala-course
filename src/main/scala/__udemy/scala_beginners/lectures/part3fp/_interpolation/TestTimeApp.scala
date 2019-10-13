package __udemy.scala_beginners.lectures.part3fp._interpolation

import scala.util.Random

object TestTimeApp extends App {
  val maxRuns = 100000000

  def plus1(i: Int) = "hello, world "+i
  def plus2(i: Int) = s"hello, world $i"

  def getWriteTime(f: Int => String): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val start = System.nanoTime()
      val s = f(it)
      System.nanoTime() - start
    }
    times.sum / 10e-9
  }

  println(getWriteTime(plus1))
  println(getWriteTime(plus2))
}
