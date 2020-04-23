package hackerrankfp.d200421_01

object JohnFenceV5 {
  def readLine = scala.io.StdIn.readLine()

  case class IH(idx: Int, height: Int)
  case class XState(idx: Int, stack: List[IH], max: Int)

  def max(max: Int, width: Int, height: Int): Int =
    scala.math.max(max, width * height)

  def calcArea(x: XState): XState = x.stack match {
    case h::Nil => XState(x.idx, Nil, max(x.max, x.idx,               h.height))
    case h::t   => XState(x.idx, t,   max(x.max, x.idx-1-t.head.idx , h.height))
  }

  @scala.annotation.tailrec
  def process1(x: XState, fence: Vector[Int]): XState =
    if (x.idx == fence.length) x // terminate loop
    else {
      val height = fence(x.idx)
      val nx: XState = if (x.stack.isEmpty || height > x.stack.head.height)
        XState(x.idx+1, IH(x.idx, height)::x.stack, x.max) else calcArea(x)
      process1(nx, fence)
    }

  @scala.annotation.tailrec
  def process2(x: XState, fence: Vector[Int]): XState = x.stack match {
    case Nil => x
    case _   => process2(calcArea(x), fence)
  }

  def calcFence(fence: Vector[Int]): XState = {
    val x0 = XState(0, List.empty, 0)
    val x1 = process1(x0, fence)
    val x2 = process2(x1, fence)
    x2
  }

  def main(args: Array[String]) {
    //                 0  1  2  3  4  5  6  7  8  9  10
    val fence = Vector(1, 2, 3, 4, 5, 6, 7, 8, 6, 4, 2)
    val max = calcFence(fence).max
    println(max)
  }

  def main_test3(args: Array[String]) {
    val _ = readLine
    val fence = readLine.split(" ").map(_.toInt).toVector
    val max = calcFence(fence).max
    println(max)
  }

  def main_test1(args: Array[String]) {
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
