package google.t1

/**
  * 50 - 0.1s
  * 60 - 0.2s
  * 70 - 0.4s
  * 80 - 1.2s
  * 90 - 1.4s
  * 100 - 10s  
  */
object Task1Test extends App {
  import Task1Domain._
  import Timed._
  import Task1NestRectAllChains._
  
  val N = 40
  
  val rects = rndRects(N).toList

  val (r, spent) = timed(inscribed(rects))
  val lgst = longest(r)
  
  println(s"Number of rectangles: $N")
  println(s"Total number of chains: ${r.length}")
  println(s"The longest chain: ${lgst}")
  println(s"Time spent: ${spent}ms")
}
