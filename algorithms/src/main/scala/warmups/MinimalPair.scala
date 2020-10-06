package warmups

import scala.util.Random

object MinimalPair extends App {
  val range = 1 to 30                                                          // range declaration
  val random30 = range map(_ => Random.nextInt(20))                            // random sequence declaration
  val mapper = (idx: Int) => idx-> (random30(idx) + random30(idx+1))           // function declaration
  val ord: Ordering[(Int, Int)] = (x: (Int, Int),y: (Int, Int)) => x._2 - y._2 // ordering declaration

  val result: (Int, Int) = (0 until range.length-1 map mapper).min(ord)
  val (idx, sum) = result

  println(random30)
  println(s"left  index $idx")
  println(s"right index ${idx+1}")
  println(s"the sum: $sum")
}
