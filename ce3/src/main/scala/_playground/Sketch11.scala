package _playground

import cats.effect.{IO, IOApp}
import cats.implicits._

import scala.concurrent.duration.DurationInt

object Sketch11 extends App {

  val data = List(1, 2, 3, 4)

  val s: String = data
    .map(x => x.toString)
    .reduce( (s1, s2) => s1 + ", "+ s2 )

  val min = data.reduce( (x1, x2) => math.min(x1, x2) )
  val max = data.reduce( (x1, x2) => math.max(x1, x2) )
  val sum = data.reduce( (x1, x2) => x1 + x2          )
  val prd = data.reduce( (x1, x2) => x1 * x2          )

  val m1: Int         = data.min
  val m2: Option[Int] = data.minOption


  println(s)   // "1, 2, 3, 4"
  println(min) // 1
  println(max) // 4
  println(sum) // 10
  println(prd) // 24


}
