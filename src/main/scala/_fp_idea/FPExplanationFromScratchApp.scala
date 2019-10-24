package _fp_idea

object FPExplanationFromScratchApp extends App {

  class Monad[T] (value: T) {
    def fold(): T = value
    def map[B](f: T => B): Monad[B] = Monad(f(value))
    def apply[B](f: T => Monad[B]): Monad[B] = f(this.value)
    def perform(f: () => T => Unit): Monad[T] = { f()(this.value); this; }
  }

  object Monad {
    def apply[T](value: T): Monad[T] = new Monad(value)
    def of[T](f: () => T): Monad[T] = new Monad(f())
  }

  val m1: Monad[Int] = Monad(1)
  val m2: Monad[Int] = m1.map((x: Int) => x + 1)
  val m3: Monad[String] = m2.map((x: Int) => x.toString)
  val m4: Monad[String] = m3.map((x: String) => s"-<$x>-")
  val folded = m4.fold()
  println(folded)
  val result = Monad(1).map(_ + 1).map(_.toString).map((x: String) => s"-<$x>-")
  println(folded)

  val z: Monad[Int] = Monad(1).apply(x => Monad(x * 2))
  val z2: Monad[Int] = Monad.of(() => { println("accessed"); 5 })
  //z2.apply(x => { println(x); Monad(null)})
  val z3: Int = z2.perform(() => x => println(x))
    .map(_ + 1)
    .perform(() => x => println(x))
    .map(_ + 1)
    .fold()
  println(z3) // 7


}
