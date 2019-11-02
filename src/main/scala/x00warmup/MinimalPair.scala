package x00warmup

import scala.util.Random

object MinimalPair extends App {
  val range = 1 to 30
  val rnd30 = range map(_ => Random.nextInt(20))

  val (idx, sum) =
    (0 until range.length-1 map (x=>(x, rnd30(x) + rnd30(x+1))))
    .min((x: (Int, Int), y: (Int, Int)) => x._2 - y._2)

  println(rnd30)
  println(s"left  index $idx")
  println(s"right index ${idx+1}")
  println(s"the sum: $sum")
}
