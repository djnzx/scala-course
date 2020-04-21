package hackerrankfp.d200421_01

import java.io.File

import scala.io.BufferedSource

object JohnFence extends App {
  def readLine = scala.io.StdIn.readLine()

  def extendFrom(fence: Vector[Int], height: Int, idx: Int): Int = {
    val zero = (true, 0)
    val foldFn: ((Boolean, Int), Int) => (Boolean, Int) = (acc, h) => acc match {
      case (true, extend) => if (height <= fence(h)) (true, extend+1) else (false, extend)
      case (false, extend) => (false, extend)
    }
    val l = Range.inclusive(idx-1, 0, -1).foldLeft(zero) { foldFn }._2
    val r = Range.inclusive(idx+1, fence.length-1, 1).foldLeft(zero) { foldFn }._2
    List(1, l, r).sum * height
  }

  def calcFence(fence: Vector[Int]): Int = {
    fence.zipWithIndex.foldLeft(0) { (acc, el) =>
      scala.math.max(acc, extendFrom(fence, el._1, el._2))}
  }
  //  platform
  //  val _ = readLine
  //  val fence = readLine.split(" ").map(_.toInt).toVector

  //  local
  val src: BufferedSource =
    scala.io.Source.fromFile(new File("src/main/scala/hackerrankfp/d200421_01/test2"))
  val _ = src.getLines().take(1).next()
  val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
  val max = calcFence(fence)
  println(max)
}
