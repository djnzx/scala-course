package __udemy.scala_beginners.lectures.part3fp._interpolation

object NanoApp extends App {
  val x1 = System.nanoTime()
  val x2 = System.nanoTime()
  println(System.nanoTime())
  println(System.nanoTime())
  println(System.nanoTime()-System.nanoTime())
  println(x2-x1) // 2000 ns = 2 micro s
}
