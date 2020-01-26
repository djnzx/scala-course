package udemy.scala_beginners.lectures.part2oop._done

import scala.io.StdIn
import scala.io.StdIn._

object IOexmples extends App {
  val name = readLine("What's your name? ")
  println(name)

  val s = StdIn.readLine("enter the desired %s: ", "value")
  println(s)
}
