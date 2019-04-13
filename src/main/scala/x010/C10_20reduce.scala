package x010

import scala.util.Random

object C10_20reduce extends App {
  val seq = Range.inclusive(1,10).map(_ * Random.nextInt(10))
  println(seq)
//  seq.reduce() === reduceLeft()
  def my_min(a: Int, b: Int) = {
    val r = if (a > b) a else b
    println(s"a: $a, b: $b, r: $r")
    r
  }
  val seq2 = seq.reduceLeft(my_min)
  println(seq2)
//  seq.reduceRight()
  val seq3 = seq.foldLeft(1)((a, b) => a * b * 2)
  println(seq3)

  def mult(a: Int, b: Int) = {
    val r = a * b
    println(s"a: $a, b: $b, r: $r")
    r
  }
  val seqx = Range.inclusive(2,5)
  val seq4 = seqx.scanLeft(5)(mult)
  println(seq4)

}
