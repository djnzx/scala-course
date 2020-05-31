package monads_basic

/**
  * the problem:
  * having F[A], G[B], and f: (A, B) => C
  *
  * we can get only:
  * F[G[A]] or G[F[A]]
  */
object CombineApp extends App {
  println(s"o2: $o2")
  println(s"e3: $e3")
  println("---")

  // we need to stack them in this way
  val r1: Option[Either[String, Int]] =
    o2.map { ov =>
      e3.map { ev =>
        f(ov, ev)
      }
    }

  // we need to stack them in that way
  val r2: Either[String, Option[Int]] =
    e3.map { ev =>
      o2.map { ov =>
        f(ov, ev)
      }
    }

  println(r1)
  println(r2)
}
