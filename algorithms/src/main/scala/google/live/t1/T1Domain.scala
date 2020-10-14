package google.live.t1

import tools.spec.ASpec

import scala.util.Random

object T1Domain {
  type R = (Int, Int) // rectangle, width and height

  def gt(bg: R, sm: R) = bg._1 > sm._1 && bg._2 > sm._2

  def nextTo(n: Int = 1000) = Random.nextInt(n)
  def rndRect() = (nextTo(), nextTo())
  def rndRects(n: Int) = LazyList.fill(n)(rndRect())
  
  def prettyPath[A](data: Seq[A]) = data.mkString(" > ")
}

class T1DomainSpec extends ASpec {
  import  T1Domain._
  
  it("1") {
    prettyPath("abcde".toList) shouldEqual "a > b > c > d > e"
  }
}
