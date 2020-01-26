package aa_cookbook.x010

import scala.util.Random

object C10_28 extends App {
  val seq = Range.inclusive(1,20).map(_ => Random.nextInt(20))
  println(seq)
  println(seq.sorted)
  println(seq.sortWith(_ > _))
  println(seq.sortBy(el => (el, el % 2))) // two sortBy



  println(seq.mkString)
  println(seq.mkString("_"))
  println(seq.mkString("<",":",">"))
}
