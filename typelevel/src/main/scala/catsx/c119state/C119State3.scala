package catsx.c119state

import cats.data.State
import catsx.c119state.C119State1._

/** useful manipulators */
object C119State3 extends App {

  /** state inspector, with function */
  val inspector: State[Total, String] = State.inspect(s => s"The state is: ${s.t} !")

  /** state extractor, actually inspector with identity */
  val extractor: State[Total, Total] = State.get[Total]

  /** direct setter */
  def setter(newState: Total): State[Total, Unit] = State.set[Total](newState)

  /** state mapper */
  val mapper: State[Total, Unit] = State.modify[Total](s => Total(s.t * 1000))

  /** lifting any value WITHOUT touching the State */
  val pure: State[Total, String] = State.pure[Total, String]("42!")

}
