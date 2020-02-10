package aa_fp

object Fps085App extends App {

  case class GolfState(value: Int)

  def up(delta: Int): StateX[GolfState, Int] = {
    println(s"up function...delta=$delta")

    StateX { gs =>
      val dist2 = gs.value + delta
      (GolfState(dist2), dist2)
    }
  }

  def down(delta: Int): StateX[GolfState, Int] = {
    println("down function...")

    StateX { gs =>
      val d2 = gs.value - delta
      (GolfState(d2), d2)
    }
  }

  val state0: GolfState = GolfState(0)

  // combinator 2: actually. It calls only `StateX:apply` 2 times
//  val transform2: StateX[GolfState, Int] = for {
//    _     <- swing(2)
//    total <- down(1)
//  } yield total

  // combinator 3: actually. It calls only `StateX:apply` 1 time
//  val transform: StateX[GolfState, Int] = for {
//    _     <- transform1
//    total <- transform2
//  } yield total

  /**
    * combinator 1
    * this code produces calls:
    *
    * up function...delta=20
    * StateX:apply(...)
    * StateX:constructor
    *
    */
  println("-> building transformation with 1 call of function:")
  val t1: StateX[GolfState, Int] = up(20)

  /**
    * combinator 2
    * this code produces calls:
    *
    * up function...delta=15
    * StateX:apply(...)
    * StateX:constructor
    * StateX:apply(...)
    * StateX:constructor
    *
    */
  println("-> building transformation with 2 calls of function:")
  val t2: StateX[GolfState, Int] = for {
    _     <- up(15)
    total <- up(2)
  } yield total

  /**
    * combinator 3
    * this code produces calls:
    *
    * StateX:apply(...)
    * StateX:constructor
    * StateX:apply(...)
    * StateX:constructor
    */
//  println("-> building transformation with MORE THAN 2 calls of function:")
//  val t3: StateX[GolfState, Int] = for {
//    _     <- up(11)
//    _     <- up(22)
//    _     <- up(13)
//    total <- up(14)
//  } yield total

  /**
    * combinator 3
    * this code produces calls:
    *
    * StateX:apply(...)
    * StateX:constructor
    */
//  println("-> building combination of transformations 1+2 calls of function:")
//  val t12: StateX[GolfState, Int] = for {
//    _     <- t1
//    total <- t2
//  } yield total

  /**
    * one call
    * swing...
    */
//  println("-> running T1 transformation:")
//  val state1: (GolfState, Int) = t1.run(state0)
//  println("-> running T2 transformation:")
//  val state2: (GolfState, Int) = t2.run(state0)
//  println("-> running T2 transformation:")
//  val state3: (GolfState, Int) = t3.run(state0)

//  println("-> results:")
//  println(s"Tuple:    $state1")       // (GolfState(36),36)
//  println(s"GolfState:${state1._1}")  // GolfState(36)
//  println(s"Value:    ${state1._2}")  // 36
}
