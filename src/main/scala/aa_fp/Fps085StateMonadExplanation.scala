package aa_fp

object Fps085StateMonadExplanation extends App {
  import Console._

  case class GolfState(value: Int)

  val inc = (delta: Int) => {
    println(s"${YELLOW}inc:func:decl:delta=$delta$RESET")
    StateX[GolfState, Int] { st =>
      val val2 = st.value + delta
      println(s"${RED}inc:func:impl:${st.value}+$delta=$val2$RESET")
      (GolfState(val2), val2)
    }
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
