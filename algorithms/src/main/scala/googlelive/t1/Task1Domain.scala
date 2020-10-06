package googlelive.t1

import scala.util.Random

object Task1Domain {
  type R = (Int, Int) // rectangle, width and height

  def gt(bg: R, sm: R) = bg._1 > sm._1 && bg._2 > sm._2

  def nextTo(n: Int = 1000) = Random.nextInt(n)
  def rndRect() = (nextTo(), nextTo())
  def rndRects(n: Int) = LazyList.fill(n)(rndRect())

}
