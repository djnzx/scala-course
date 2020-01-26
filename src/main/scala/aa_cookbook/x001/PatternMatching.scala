package aa_cookbook.x001

import scala.util.Random

object PatternMatching extends App {
  val x: Int = Random.nextInt(5)

  val z = x match {
    case 0 => "zero"
    case 1 => "one"
    case 2 => "two"
    case _ => "other"
  }

  println(z)
}
