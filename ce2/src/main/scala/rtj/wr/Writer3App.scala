package rtj.wr

import cats.data.Writer
import cats.implicits._

object Writer3App extends App {

  def sum(n: Int): Int =
    if (n == 0) {
      println("done")
      0
    } else {
      println(s"diving $n -> ${n - 1}")
      sum(n - 1) + n
    }

  val r1 = sum(5)
  println(r1)

  /** The pattern is:
    *
    *   - function always takes bare type
    *   - function always returns Writer `box`
    *   - all messages lifted to Vector(msg).tell
    *   - text combined automatically by flatMap
    *   - value mapped via .map
    */
  def sumWithLogs(n: Int): Writer[Vector[String], Int] =
    if (n == 0) {
      Writer(Vector("done"), 0)
    } else {
      val w1 = Vector(s"diving $n -> ${n - 1}").tell
      val w2 = sumWithLogs(n - 1).map(_ + n)
      w1 >> w2
    }

  val (log, value) = sumWithLogs(5).run
  println(value)
  println(log.mkString("\n"))

}
