package cats101.c119state

import cats.data.State

object C119State4 extends App {

  /** state */
  case class Total(t: Int) {
    def added(n: Int) = Total(t + n)
    def muled(n: Int) = Total(t * n)
  }

  def add(n: Int) = State.modify[Total](_.added(n))
  def mul(n: Int) = State.modify[Total](_.muled(n))

  val add2: State[Total, Unit] = add(2)
  val mul2: State[Total, Unit] = mul(2)

  /** you can grab intermediate states, and still construct the combined function */
  val combined = for {
    _ <- add(10)  // + 10 / a = 100 + 10 = 110
    _ <- mul2     // *2   / b = 110 * 2  = 220
    _ <- add2     // +1   / c = 220 + 2  = 222
  } yield ()

  val s0 = Total(100)

  /** final State only: State(222) */
  val r2 = combined.runS(s0).value
  println(r2)
}
