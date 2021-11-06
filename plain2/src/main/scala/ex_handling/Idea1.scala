package ex_handling

object Idea1 extends App {
  
  def wrapped[A, B, E](f: A => B, h: E => B): A => B =
    (a: A) => 
      try { f(a) } 
      catch { case ex: E => h(ex) }
    
  val w1: Int => Int = wrapped(
    (x: Int) => x+1,
    (_: RuntimeException) => -1
  )
  
  val w2: Int => Int = wrapped(
    (x: Int) => 1/x,
    (_: RuntimeException) => -1
  )
  
  println(w1(0)) // result provided
  println(w2(0)) // default value from handler given 
  
    
  
}
