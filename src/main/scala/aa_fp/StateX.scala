package aa_fp

/**
  *                   `==============`
  * the semantics of  | S => (S, A) |  is:
  *                  `==============`
  *
  * to calculate the new state
  * based on current state
  * by applying business logic
  * by accessing to needed variables
  * via closure of parent function
  *
  */
case class StateX[S, A](run: S => (S, A)) {

  println("StateX:constructor")

  def flatMap[B](f: A => StateX[S, B]): StateX[S, B] = StateX { s0 =>
    println("StateX:flatMap(...)")
    val (s1, a): (S, A) = run(s0)
    val r: (S, B) = f(a).run(s1)
    println("StateX:flatMap(...) leaving")
    r
  }

//  def map0[B](f: A => B): StateX[S, B] = flatMap(a => StateX.point(f(a)))
  def map[B](f: A => B): StateX[S, B] = {
    println("StateX:map(...)")
    StateX { s0 =>
      val (s1, a): (S, A) = run(s0)
      val ff: A => StateX[S, B] = (a: A) => StateX { s: S => (s, f(a)) }
      val r: (S, B) = ff(a).run(s1)
      r
    }
  }
}

object StateX {
  // that's default implementation included in case class
  def apply[S, A](run: S => (S, A)): StateX[S, A] = {
    println("StateX:apply(...)")
    new StateX(run)
  }
  // that's our implementation to simplify HOF
  def point[S, A](v: A)            : StateX[S, A] = {
    println("StateX:point(...)")
    new StateX(s => (s, v))
  }
}
