package monads_basic

object CombineApp extends App {
  type NoneOr[A] = Either[Nothing, A]

  val o2: Option[Int] = Some(2)
  val e3: NoneOr[Int] = Right(3)

  println(o2)
  println(e3)

  val f: (Int, Int) => Int = (a, b) => a + b

  // we need to stack them in this way
  val r1: Option[NoneOr[Int]] =
    o2.map { ov =>
      e3.map { ev =>
        f(ov, ev)
      }
    }

  // we need to stack them in that way
  val r2: NoneOr[Option[Int]] =
    e3.map { ev =>
      o2.map { ov =>
        f(ov, ev)
      }
    }

  println(r1)
  println(r2)
}
