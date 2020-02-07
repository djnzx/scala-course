package topics.state

object StateApp extends App {

  case class ProcessState(number: Long) {
    private def modify(number: Long, delta: Long): (ProcessState, Long) = {
      val number2 = number + delta
      (ProcessState(number2), number2)
    }
    def inc(delta: Long): (ProcessState, Long) = modify(number, delta);
    def inc: (ProcessState, Long) = inc(1L)
    def dec(delta: Long): (ProcessState, Long) = modify(number, delta);
    def dec: (ProcessState, Long) = dec(1L)
  }

  val ps: ProcessState = ProcessState(42)
  ps.inc match {
    case (state, l) => println(l)
  }

}
