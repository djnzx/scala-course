package cats101.writer

import cats.data.Writer
import pprint.{pprintln => println}
import cats.instances.list._

/**
  * Writer[L, V] = WriterT[Id, L, V]
  */
object WriterApp1 extends App {
  
  /** just create a writer */
  def logNumber(x: Int): Writer[List[String], Int] = 
    Writer(List(s"Got: $x"), x)
    
  /** do the operation described in the yield and combine the log using monoid instances */
  def multLogged(a: Int, b: Int): Writer[List[String], Int] = 
    for {
      av <- logNumber(a)
      bv <- logNumber(b)
    } yield av * bv
    
  println(multLogged(3, 5).run)
}
