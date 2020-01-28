package aa_fp

object Fps004LazyVsEager extends App {

  def long_computation: Int = {
    println("body:starting")
    Thread.sleep(1000)
    println("body:finished")
    42
  }

  def f_eager(v: Int) = {
    val started = System.nanoTime()
    println("f_eager:starting")
    println(v)
    println(s"f_eager:finished. took ${System.nanoTime() - started}")
  }

  def f_lazy(v: => Int) = {
    val started = System.nanoTime()
    println("f_lazy:starting")
    println(v)
    println(s"f_lazy:finished. took ${System.nanoTime() - started}")
  }

  println("-")
  f_eager(long_computation)
  println("-")
  f_lazy(long_computation)
  println("-")

}
