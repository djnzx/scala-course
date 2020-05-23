package fps

object Fps085StateMonadCleaned extends App {

  case class StateXC[S, A] private (run: S => (S, A)) {

    def flatMap[B](f: A => StateXC[S, B]): StateXC[S, B] = StateXC { s0 =>
      val (s1, a) = run(s0) // run having
      f(a).run(s1)          // run given (dive deeper) -->
    }

    def map[B](f: A => B): StateXC[S, B] = StateXC { s0 =>
      val (s1, a) = run(s0) // run having
      (s1, f(a))
    }
  }

  case class GolfState(value: Int)

  val inc = (delta: Int) => StateXC[GolfState, Int] { st =>
    val val2 = st.value + delta
    (GolfState(val2), val2)
  }

  println("-> building transformation with 3 calls:")
  lazy val trans3 = for {
    _     <- inc(5) // flatMap
    _     <- inc(2) // flatMap
    total <- inc(1) // map
  } yield total

  val state0 = GolfState(10)
  println("-> running T2 transformation:")
  val state3 = trans3.run(state0)

  println("-> results:")
  println(s"Tuple: $state3")       // (GolfState(18),18)
  println(s"State: ${state3._1}")  // GolfState(18)
  println(s"Value: ${state3._2}")  // 18
}
