package hackerrankfp.d200421.johnfence

import hackerrankfp.d200421.johnfence.JohnFenceV1.src

import java.io.File
import tools.Timed.printTimed
import tools.Timed.timed

import scala.collection.mutable
import scala.io.BufferedSource

/** 28s */
object JohnFenceV2 extends App {
  def readLine = scala.io.StdIn.readLine()

  case class Area(height: Int, l: Int, r: Int)

  val process: mutable.Map[Int, Set[Area]] = mutable.Map.empty

  // that's cache gives you ability to improve only by 10%
  def isVisited(height: Int, idx: Int): Boolean =
    process.get(height).exists { _.exists { a: Area => (a.l <= idx) && (idx <= a.r) } }

  def extendFrom(fence: Vector[Int], height: Int, idx: Int): Int = {
    if (isVisited(height, idx)) 0
    else {
      val zero = (true, 0)
      val foldFn: ((Boolean, Int), Int) => (Boolean, Int) = (acc, h) =>
        acc match {
          case (true, extend)  => if (height <= fence(h)) (true, extend + 1) else (false, extend)
          case (false, extend) => (false, extend)
        }
      val to_l = Range.inclusive(idx - 1, 0, -1).foldLeft(zero) { foldFn }._2
      val to_r = Range.inclusive(idx + 1, fence.length - 1, 1).foldLeft(zero) { foldFn }._2

      val st: Set[Area] = process.getOrElse(height, Set.empty)
      val st2: Set[Area] = st + Area(height, idx - to_l, idx + to_r)
      process.put(height, st2)
      List(1, to_l, to_r).sum * height
    }
  }

  def calcFence(fence: Vector[Int]): Int = {
    fence.zipWithIndex.foldLeft(0) { (acc, el) =>
      scala.math.max(acc, extendFrom(fence, el._1, el._2))
    }
  }
  //  platform
  //  val _ = readLine
  //  val fence = readLine.split(" ").map(_.toInt).toVector

  //  local
  val src: BufferedSource =
    scala.io.Source.fromFile(new File("algorithms/src/main/scala/hackerrankfp/d200421_01/test2big"))
  locally {
    val _ = src.getLines().take(1).next()
  }
  val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
  printTimed(
    calcFence(
      Vector(
        1, 2, 3, 4, 5, 6, 6, 4, 3, 4, 5, 6, 7, 8, 6, 4, 2,
      ),
    ),
  )
  println(process)
  println(process.size)
//  process.foreach { case (h: Int, a: Set[Area]) => println(s"h:$h, s:${a.size}") }
  val t = process.foldLeft(0) { case (acc, (h, a: Set[Area])) => acc + a.size }
  println(t)
}
