package macros

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.Quasiquote

/** https://docs.scala-lang.org/overviews/quasiquotes/intro.html */
object QuasiQuotes extends App {
  val x: universe.Tree = q"""
         val x: List[Int] = List(1, 2) match {
           case List(a, b) => List(a + b)
         }
       """
  println(x)
}
