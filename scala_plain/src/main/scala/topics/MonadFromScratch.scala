package topics

object MonadFromScratch extends App {

  class Monad[A] (core: () => A) {
    def fold(): A = core()
    def map[B](f: A => B)          : Monad[B] = Monad(f(core()))
    def apply[B](f: A => Monad[B]) : Monad[B] = f(core())
    def perform(f: () => A => Unit): Monad[A] = { f()(core()); this; }
  }

  object Monad {
    def apply[T](value: T): Monad[T] = new Monad(() => value)
  }

  val r: String = Monad(1)
    .apply(x => Monad(x + 1))
    .map(_ + 1)
    .perform(() => x => println(x))
    .map(x => x.toString)
    .map(x => s"-=<$x>=-")
    .fold()
  println(r)
}
