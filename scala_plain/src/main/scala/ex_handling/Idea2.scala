package ex_handling

import scala.util.Try
import pprint.{pprintln => println}

object Idea2 extends App {
  
  def div(a: Int, b: Int): Int = a / b 
  
  // bad...
  // div(1, 0)

  def div2(a: Int, b: Int): Either[Throwable, Int] = Try(div(a,b)).toEither
  // better, but inconvenient
  // div2(1, 0)
  
  val rethrow = (t: Throwable) => throw t
  def div3(a: Int, b: Int, recover: Throwable => Int = rethrow): Int = div2(a, b).fold[Int](recover, identity) 
  
  /** everything okay */
  println( div3(10, 5) )
  /** we DO expect failing */
  println( div3(3, 0, _ => -1) )
  /** we DON'T expect failing */
  println( div3(3, 0) )
  
}
