package topics.state_monad_evo

case class State[S, A](run: S => (S, A)) {
  def flatMap[B](f: A => State[S,B]): State[S,B] = State { st =>
    val (s0, a) = run(st)
    f(a).run(s0)
  }
  def map[B](f: A => B): State[S,B] = State { st =>
    val (s0, a) = run(st)
    (s0, f(a))
  }
}
