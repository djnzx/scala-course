package monad_trans.monads

class IO[A] private (body: => A) {

  def run: A = body

  def flatMap[B](f: A => IO[B]): IO[B] = {
    val iob: IO[B] = f(run)
    val b: B = iob.run
    IO(b)
  }

  def map[B](f: A => B): IO[B] = flatMap(a => IO(f(a)))

}

object IO {
  def apply[A](a: => A): IO[A] = new IO(a)
}

