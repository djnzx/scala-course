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
  * constructor is called in each map/flatMap call
  * and in each appropriate function call
  */
case class StateX[S, A] private (run: S => (S, A)) {

  import Console._

  println(s"${BLUE}SX:constructor$RESET")

  def flatMap[B](f: A => StateX[S, B]): StateX[S, B] = StateX { s0 =>
    println(s"SX:$s0:flatMap:1/5:applying having:run(s0)...")
    val (s1, a): (S, A) = run(s0)
    println(s"SX:$s0:flatMap:2/5:applied $GREEN[$s1:$a]$RESET")
    println(s"SX:$s0:flatMap:3/5:calculating given:f(a)")
    val sb: StateX[S, B] = f(a)
    println(s"SX:$s0:flatMap:4/5:applying given:f(a).run(s1)...[RECURSION]")
    val r: (S, B) = sb.run(s1) // go nested -->
    println(s"SX:$s0:flatMap:5/5:$GREEN[${r._1}:${r._2}]$RESET leaving...")
    r
  }

  def map[B](f: A => B): StateX[S, B] = flatMap(a => StateX.lift(f(a)))
//  def map[B](f: A => B): StateX[S, B] = StateX { s0 =>
//    println(s"SX:$s0:map:1/2:applying having:run(s0)...")
//    val (s1, a) = run(s0)
//    println(s"SX:$s0:map:2/2:applied $GREEN[$s1:$a]$RESET leaving...")
//    (s1, f(a))
//  }
}

object StateX {
  def lift[S, A](a: A): StateX[S, A] = StateX { s => (s, a) }
}
