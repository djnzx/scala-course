package hackerrankfp.d200421_01

import scala.annotation.tailrec

object JohnFenceV4 {
  def readLine = scala.io.StdIn.readLine()

  def calcArea(oldMax: Int, idx: Int, s: List[Int], topHeight: Int): Int = {
    val width = if (s.isEmpty) idx else idx-1 -s.head
    scala.math.max(oldMax, width * topHeight)
  }

  case class XState(idx: Int, stack: List[Int], max: Int)

  @tailrec
  def process1(x: XState, fence: Vector[Int]): XState =
    if (x.idx < fence.length) process1(
      if (x.stack.isEmpty || fence(x.stack.head) < fence(x.idx))
        XState(x.idx+1, x.idx::x.stack, x.max) else
        XState(x.idx,   x.stack.tail,   calcArea(x.max, x.idx, x.stack.tail, fence(x.stack.head)))
      , fence)
    else x

  @tailrec
  def process2(x: XState, fence: Vector[Int]): XState = x.stack match {
    case Nil => x
    case _   => process2(
      XState(x.idx, x.stack.tail, calcArea(x.max, x.idx, x.stack.tail, fence(x.stack.head)))
      , fence
    )
  }

  def calcFence(fence: Vector[Int]): XState = {
    val x0 = XState(0, List.empty, 0)
    val x1 = process1(x0, fence)
    process2(x1, fence)
  }

  def main(args: Array[String]) = {
    val _ = readLine
    val fence = readLine.split(" ").map(_.toInt).toVector
    val max = calcFence(fence).max
    println(max)
  }

  def main_test1(args: Array[String]) = {
    val fence = Vector(1, 2, 3, 4, 5, 6, 5, 4, 3, 0, 4, 5, 6, 7, 8, 6, 4, 2)
    val max = calcFence(fence).max
    println(max)
  }

  def main_test2(args: Array[String]) {
      val src = scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200421_01/test2big"))
      val _ = src.getLines().take(1).next()
      val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
    val max = calcFence(fence).max
    println(max)
  }
}
