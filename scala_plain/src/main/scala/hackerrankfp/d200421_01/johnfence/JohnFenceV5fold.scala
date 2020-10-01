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
    def process1(i: Int, s: List[Int], ma: Int): (Int, List[Int], Int) =
      if (i < fence.length)
        s match {
          case Nil                         => process1(i+1, i::s, ma)
          case h::_ if fence(h) < fence(i) => process1(i+1, i::s, ma)
          case h::t  =>
            val ma2 = calcAndMaxArea(ma, i, t, fence(h))
            process1(i,   t,    ma2)
        }
      else (i, s, ma)

    val s0 = XState(0, List.empty, 0)

    // count max correctly if we finish with descending
    val s1: (Int, List[Int], Int) = process1(s0.idx, s0.stack, s0.maxArea)

    // find max if we only climb
    val s2 = s1._2.foldLeft(XState.tupled(s1)) { case (XState(idx, h :: t, maxArea), _) =>
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

  def main_test2(args: Array[String]) = {
      val src = scala.io.Source.fromFile(new java.io.File("scala_plain/src/main/scala/hackerrankfp/d200421_01/test2big"))
      val _ = src.getLines().take(1).next()
      val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }
}
