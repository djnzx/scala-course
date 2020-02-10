package aa_fp

object Fps083State extends App {

  case class State[A](a: A) {
    def map[B](f: A => B): State[B] = State(f(a))
    def flatMap[B](f: A => State[B]): State[B] = State(f(a).a)
    def flatMap2[B](f: A => State[B]): State[B] = f(a)
  }

  val s = State(10)
  val s4 = for {
    s1 <- s
    s2 <- State(s1 + 10)
    s3 <- State(s2 + 5)
  } yield s3

  println(s4)

}
