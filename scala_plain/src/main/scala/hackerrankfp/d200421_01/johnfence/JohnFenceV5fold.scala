package hackerrankfp.d200421_01.johnfence

import pprint.{pprintln => println}

object JohnFenceV5fold {
  def readLine = scala.io.StdIn.readLine()

  def calcFence(fc: Vector[Int]) = {

    def pass1(i: Int, s: List[Int], mx: Int): (List[Int], Int) = 
      (i - fc.length, s) match {
        case (0, _)                     => (s, mx)
        case (_, Nil)                   => pass1(i+1, i::s, mx)
        case (_, h::_) if fc(i) > fc(h) => pass1(i+1, i::s, mx)
        case (_, h::Nil)                => pass1(i,   Nil,  mx max fc(h) * i)
        case (_, h::(t@g::_))           => pass1(i,   t,    mx max fc(h) * (i -1 -g))
      }

    pass1(0, Nil, 0) match { case (stack, mx) =>
      stack.foldLeft(mx) { case (pmax, h) =>
        pmax max fc(h) * (fc.length - 1 - h)
      }
    }
    
  }

  def main1(args: Array[String]) = {
    val _ = readLine
    val fence = readLine.split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }

  def main(args: Array[String]) = {
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
