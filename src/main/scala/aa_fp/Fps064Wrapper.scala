package aa_fp

object Fps064Wrapper extends App {

  class Wrapper[A] private (value: A) {
    def map[B](f: A => B): Wrapper[B] = new Wrapper(f(value))
    def flatMap[B](f: A => Wrapper[B]): Wrapper[B] = f(value)
    override def toString: String = s"${this.getClass.getSimpleName}[$value]"
  }
  object Wrapper {
    def apply[A](value: A): Wrapper[A] = new Wrapper(value)
  }

  val r: Wrapper[Int] = for {
    a <- Wrapper(1)
    b <- Wrapper(2)
    c <- Wrapper(3)
    d = a + b + c
  } yield d

  println(r)
}
