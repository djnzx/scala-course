package aa_cookbook.x003

import scala.util.Random

object Intro extends App {
  val r = Random
  val n = r.nextInt(20)
  val s = if (n<10) "lt 10" else "gt 10"
  println(s)

//  for (line <- source.lines) {
//    for {
//      char <- line
//          //....
//    }
//  }
//
//  for {
//    line <- source.lines
//    char <- line
//    if char.isLetter
//  }



}
