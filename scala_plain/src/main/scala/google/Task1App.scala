package google

import scala.util.Random

/**
  *  50 - 0.1s
  *  60 - 0.2s
  *  70 - 0.4s
  *  80 - 1.2s
  *  90 - 1.4s
  * 100 -  
  *  
  */
object Task1App extends App {
  import  Task1NestedRectangles._
  
  def next() = Random.nextInt(1000)
  def randomT() = (next(), next()) 
  def time() = System.currentTimeMillis()
  val rects = LazyList.fill(40)(randomT()).toList
  val start = time()
  val r = inscribed(rects)
  val delta = time() - start
  println(r.length)
  val lgst = longest(r)
  println(lgst.length)
  println(lgst)
  println(s"time:${delta}ms")
}
