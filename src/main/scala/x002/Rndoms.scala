package x002

import scala.util.Random

object Rndoms extends App {
  val r = Random
  println(r.nextInt)
  println(r.nextInt(100))

  println(r.nextPrintableChar())
  val range = 0 to r.nextInt(20)

  (for (i <- range) yield r.nextPrintableChar()).foreach(print(_))
  println
  (for (i <- range) yield i*2).foreach(el => print(s"${el} "))
  println

  var rg1 = 1 to 10
  // rg1: scala.collection.immutable.Range.Inclusive = Range 1 to 10
  var rg2 = 1 to 10 by 2
  // rg2: scala.collection.immutable.Range = inexact Range 1 to 10 by 2
  var rg3 = 1 until 10

  var a1 = rg1.toList
  println(a1)
  var a2 = rg1.toArray
  println(a2.mkString(","))



}
