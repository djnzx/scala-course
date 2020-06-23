package lux

import scala.language.implicitConversions

object LuxApp extends App {
  
  // 1. trait
  trait Show[A] {
    def show(a: A): String
  }

  // 2. instances
  implicit val si: Show[Int] = a => s"INT: $a"
  implicit val ss: Show[String] = a => s"STRING: $a"
  
  // 3. syntax v.1 
//  implicit class ShowSyntax[A: Show](a: A) {
//    def show: String = implicitly[Show[A]].show(a)
//  }
  // 3. syntax v.2 
  class ShowSyntax[A: Show](a: A) {
    def show: String = implicitly[Show[A]].show(a)
  }
  
  implicit def showSyntax[A: Show](a: A) = new ShowSyntax(a)
  
  val x1: String = 1.show 
  val x2: String = "Hello".show 
}
