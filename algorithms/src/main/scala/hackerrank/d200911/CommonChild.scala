package hackerrank.d200911

import pprint.{pprintln => println}

/**
  * https://www.hackerrank.com/challenges/common-child/problem
  */
object CommonChild extends App {

  def commonChild(s1: String, s2: String): Int = {
    val dimX = s1.length + 1
    val dimY = s2.length + 1
    val m = Array.ofDim[Int](dimY, dimX)
    (1 until dimY).foreach { y =>
      (1 until dimX).foreach { x =>
        m(y)(x) =
          if (s1(x-1) == s2(y-1)) m(y-1)(x-1) + 1
          else m(y-1)(x) max m(y)(x-1) 
      }
    }
    m(dimY-1)(dimX-1)
  }


  println(commonChild("ABCDEF","FBDAMN"))


}
