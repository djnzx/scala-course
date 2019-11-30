package _udemy.scala_beginners.lectures.part3fp._map_flapmap_flatten

import scala.util.Random

object RandomApp extends App {
  val r = new Random()
  val vector = for (i <- 1 to 5) yield r.nextInt(100)
  val sorted = vector.sorted
  println(vector)
  println(sorted)
}
