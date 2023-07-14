package monad_basics

/**
  * the problem:
  * having F[A], G[B], and f: (A, B) => C
  *
  * approach 2: widen them and flatMap
  * wide F[A] to G[A]
  * and get G[C]
  */
object B2_Widening extends App {
  println(s"o2: $o2")
  println(s"e3: $e3")
  println("---")

  val e2: Either[String, Int] = o2 match {
    case Some(value) => Right(value)
    case None        => Left("no data")
  }
  println(s"e2: $e2")

  val r: Either[String, Int] = for {
    v2 <- e2
    v3 <- e3
  } yield f(v2, v3)

  println(r)
}
