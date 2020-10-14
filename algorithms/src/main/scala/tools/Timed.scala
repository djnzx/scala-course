package tools

object Timed {

  def time(): Long = System.currentTimeMillis

  def timed[A](body: => A): (A, Long) = {
    val start = time()
    val a: A = body
    val spent = time() - start
    (a, spent)
  }
  
  def printTimed[A](body: => A, name: String = "res") = {
    val (a, spent) = timed(body)
    println(s"$name: $a")
    println(s"Spent: ${spent}ms")
  }

}
