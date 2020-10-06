package googlelive.t1

object Timed {
  
  def time(): Long = System.currentTimeMillis()
  def timed[A](body: => A): (A, Long) = {
    val start = time()
    val a: A = body
    val spent = time() - start
    (a, spent)
  }

}
