package x0lessons.fp_to_the_max

package object v1 {

  case class IO[A](core: () => A) { self =>
    def map[B](f: A => B): IO[B] = IO(() => f(self.core()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.core()).core())
  }

  object IO {
    def point[A](a: => A): IO[A] = IO( () => a)
  }

}
