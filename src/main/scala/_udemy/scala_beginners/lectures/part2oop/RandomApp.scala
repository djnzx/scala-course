package _udemy.scala_beginners.lectures.part2oop

import scala.collection.immutable.List
import scala.util.Random

object RandomApp extends App {
  val s =  List.fill(10)((Random.nextInt('Z' - 'A') + 'A').toChar).mkString
  println(s)
}
