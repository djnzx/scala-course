package _fp_idea

object FPExplanationFromScratchApp extends App {

  class Monad[T] (value: T) {
    def fold(): T = value
    def map[B](f: T => B) = Monad(f(value))
    def apply[B](f: Monad[T] => Monad[B]) = Monad(f(this))
  }

  object Monad {
    def apply[T](value: T): Monad[T] = new Monad(value)
  }

  val m1: Monad[Int] = Monad(1)
  val m2: Monad[Int] = m1.map((x: Int) => x + 1)
  val m3: Monad[String] = m2.map((x: Int) => x.toString)
  val m4: Monad[String] = m3.map((x: String) => s"-<$x>-")
  val folded = m4.fold()
  println(folded)
  val result = Monad(1).map(_ + 1).map(_.toString).map((x: String) => s"-<$x>-")
  println(folded)



}
