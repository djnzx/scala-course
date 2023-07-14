package catsx.c119state

import cats.Eval
import catsx.c119state.C119State1._

object C119State2 {

  /** we can "compile" State to "Eval" */
  val eval: Eval[(Total, (Int, Int, Int))] = combined2a.run(Total(100))
  /** to value only */
  val evalA: Eval[(Int, Int, Int)] = combined2a.runA(Total(100))
  /** to state only */
  val evalS: Eval[Total] = combined2a.runS(Total(100))

  /** end finally eval it a value */
  val (state, value) = eval.value

  println(state)
  println(value)

  println(eval.value)
  println(evalA.value)
  println(evalS.value)

}
