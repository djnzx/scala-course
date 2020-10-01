package hackerrankfp.d200421_01.johnfence


object JohnFenceV5fold {
  import scala.annotation.tailrec
  def readLine = scala.io.StdIn.readLine()

  def calcAndMaxArea(prevMaxArea: Int, idx: Int, s: List[Int], topHeight: Int): Int = {
    val width = s match {
      case Nil    => idx
      case h :: _ => idx - 1 - h
    } 
    prevMaxArea max width * topHeight
  }

  case class XState(idx: Int, stack: List[Int], maxArea: Int)

  def calcFence(fence: Vector[Int]) = {

    @tailrec
    def process1(x: XState): XState =
      if (x.idx < fence.length) process1(
        if (x.stack.isEmpty || fence(x.stack.head) < fence(x.idx))
          XState(x.idx+1, x.idx::x.stack, x.maxArea) else
          XState(x.idx,   x.stack.tail,   calcAndMaxArea(x.maxArea, x.idx, x.stack.tail, fence(x.stack.head))))
      else x

    val s0 = XState(0, List.empty, 0)
    
    val s1 = process1(s0)

    val s2 = s1.stack.foldLeft(s1) { case (XState(idx, h :: t, maxArea), _) =>
      XState(idx, t, calcAndMaxArea(maxArea, idx, t, fence(h)))
    }
    
    s2.maxArea
  }

  def main(args: Array[String]) = {
    val _ = readLine
    val fence = readLine.split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }

  def main_test1(args: Array[String]) = {
    val fence = Vector(1, 2, 3, 4, 5, 6, 5, 4, 3, 0, 4, 5, 6, 7, 8, 6, 4, 2)
    val max = calcFence(fence)
    println(max)
  }

  def main_test2(args: Array[String]) {
      val src = scala.io.Source.fromFile(new java.io.File("scala_plain/src/main/scala/hackerrankfp/d200421_01/test2big"))
      val _ = src.getLines().take(1).next()
      val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }
}
