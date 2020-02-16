package aa_fp

object Fps085StateMonadExplanation extends App {
  import Console._

  case class GolfState(value: Int)

  val inc: Int => StateX[GolfState, Int] = (delta: Int) => {
    println(s"${YELLOW}inc:func:decl:delta=$delta$RESET")
    StateX[GolfState, Int] { st =>
      val val2 = st.value + delta
      println(s"${RED}inc:func:impl:${st.value}+$delta=$val2$RESET")
      (GolfState(val2), val2)
    }
  }

  println("-> building transformation with 3 calls:")
  lazy val trans31 = for {
    _     <- inc(5) // flatMap
    _     <- inc(2) // flatMap
    total <- inc(1) // map, we can easily get rid of total
  } yield total     // and produce the clean output

  println("-> building transformation with 3 calls:")
  lazy val trans32 = inc(5).flatMap(_ => inc(2).flatMap(_ => inc(1)))

  val state0 = GolfState(10)
  println("-> running T3 transformation:")
  val state31 = trans31.run(state0)
  println("-> running T3 transformation:")
  val state32 = trans32.run(state0)
  println("-> results:")
  println(s"Tuple: $state31")       // (GolfState(18),18)
  println(s"State: ${state31._1}")  // GolfState(18)
  println(s"Value: ${state31._2}")  // 18
  println(s"Tuple: $state32")       // (GolfState(18),18)
  println(s"State: ${state32._1}")  // GolfState(18)
  println(s"Value: ${state32._2}")  // 18

  println("-> EXPERIMENTS")
  /**
    * because StateX operates in terms of: S => (S, A)
    * we cant just apply one and use it
    *
    * we always build function which takes initial state
    * and returns NEW STATE + VALUE
    *
    */
  case class Accum(value: Int)

  // function
  val swing = (delta: Int) => StateX { st: Accum =>
    val value2 = st.value + delta
    (Accum(value2), value2)
  }

  /**
    * initial state
    */
  val a0 = Accum(2)
  /**
    * usage 1: we create function which knows how to
    * affect given state
    * add apply it to initial state Accum(2)
    *
    * once we `run` - we are `opening` monad
    */
  println(":1:direct usage")
  lazy val z1 = swing(5)
  println(z1.run(a0))
  /**
    * usage 2:
    * we chain several functions
    * add apply it to initial state Accum(2)
    *
    * map does change the value
    * but doesn't change the monad
    */
  println(":2:maps compositions")
  lazy val z2 = swing(5).map(_ + 1).map(_ + 1).map(_ + 1)
  println(z2.run(a0))

  println(":3:tlatMaps compositions")
  lazy val z3 = swing(3).flatMap(_ => swing(4).flatMap(_ => swing(5)))
  println(z3.run(a0))


//  val z: (Int, Int) = StateX.lift(7) // Monad
//    .map(_ +1)  // Monad
//    .run(2)
//  println(z)

  // single mapping
//  val gs2 = StateX.lift(5).run(GolfState(1))
  //  val gs3: StateX[GolfState, Int] = StateX.lift(GolfState(10)).flatMap(gs1 => StateX { _ => (GolfState(gs1.value+1),gs1.value+1) })
//  val frf: GolfState => (GolfState, Int) = gs3.run
//  val frr = frf(GolfState(-999))

//  println(s"gs2 = ${gs2._1}")


}
