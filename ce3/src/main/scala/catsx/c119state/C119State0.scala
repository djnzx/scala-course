package catsx.c119state

import cats.data.State

object C119State0 extends App {

  /** state */
  case class Total(t: Int) {
    def added(n: Int) = Total(t + n)
    def subed(n: Int) = Total(t - n)
    def muled(n: Int) = Total(t * n)
  }

  def add(n: Int) = State { s: Total =>
    val s2 = s.added(n)
    (s2, s2.t)
  }

  def mul(n: Int) = State { s: Total =>
    val s2 = s.muled(n)
    (s2, s2.t)
  }

  val add2: State[Total, Int] = add(2)
  val mul2: State[Total, Int] = mul(2)

  /** you can grab intermediate states, and still construct the combined function */
  val combined2a: State[Total, (Int, Int, Int)] = for {
    a <- add(10)  // + 10 / a = 100 + 10 = 110
    b <- mul2     // *2   / b = 110 * 2  = 220
    c <- add2     // +1   / c = 220 + 2  = 222
    r = (a, b, c)
  } yield r

  val s0 = Total(100)

  /** result of the last map only: (110,220,222) */
  val r1 = combined2a.runA(s0).value
  println(r1)

  /** final State only: State(222) */
  val r2 = combined2a.runS(s0).value
  println(r2)

  /** State + value: (Total(222),(110,220,222)) */
  val r3 = combined2a.run(s0).value
  println(r3)

}
