package aa_fp

object Fps085App extends App {

  case class GolfState(value: Int)

  /**
    * 1. it takes current state and delta
    * 2. it calculates new state (business logic)
    * 3. it returns tuple with new state and new value
    */
  val up_impl = (state: GolfState, delta: Int) => {
    val val2 = state.value + delta
    (GolfState(val2), val2)
  }

  /**
    * 1. it takes current state and delta
    * 2. it calculates new state (business logic)
    * 3. it returns tuple with new state and new value
    */
  val down_impl = (state: GolfState, delta: Int) => {
    val val2 = state.value - delta
    (GolfState(val2), val2)
  }

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
  def up(delta: Int): StateX[GolfState, Int] = {
    println(s"up function...delta=$delta")
    StateX { st => up_impl(st, delta) }
  }

  def down(delta: Int): StateX[GolfState, Int] = {
    println("down function...")
    StateX { st => down_impl(st, delta) }
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
  println("-> running T1 transformation:")
  val state1: (GolfState, Int) = t1.run(state0)
  println("-> running T2 transformation:")
  val state2: (GolfState, Int) = t2.run(state0)
//  println("-> running T2 transformation:")
//  val state3: (GolfState, Int) = t3.run(state0)

  println("-> results:")
  println(s"Tuple:    $state1")       // (GolfState(20),20)
  println(s"GolfState:${state1._1}")  // GolfState(20)
  println(s"Value:    ${state1._2}")  // 20

  println(s"Tuple:    $state2")       // (GolfState(17),17)
  println(s"GolfState:${state2._1}")  // GolfState(17)
  println(s"Value:    ${state2._2}")  // 17
}
