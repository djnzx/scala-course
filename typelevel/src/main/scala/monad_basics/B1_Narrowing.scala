package monad_basics

/**
  * the problem:
  * having F[A], G[B], and f: (A, B) => C
  *
  * approach 1: narrow and flatMap
  * narrow G[B] to F[B]
  * and get F[C]
  */
object B1_Narrowing extends App {
  println(s"o2: $o2")
  println(s"e3: $e3")
  println("---")

  val o3: Option[Int] = e3.toOption
  println(s"o3: $o3")

  val r: Option[Int] = for {
    v2 <- o2
    v3 <- o3
  } yield f(v2, v3)

  println(r)
}
