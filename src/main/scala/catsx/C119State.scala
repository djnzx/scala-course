package catsx

import cats.Eval
import cats.data.State

object C119State extends App {
  case class Total(t: Int)

  val add1 = State[Total, Int] { s =>
    val r = s.t + 1
    (Total(r), r)
  }

  val add2 = () => State[Total, Int] { s =>
    val r = s.t + 2
    (Total(r), r)
  }

  val addN = (n: Int) => State[Total, Int] { s =>
    val r = s.t + n
    (Total(r), r)
  }

  val combined = for {
    x <- addN(10) // x = initial + 10 // state +10
    _ <- add2()                       // state +2
    n <- add1                         // state +1
  } yield n + x                       // state, value = state.value + state from 1st step(43)

  val zero = Total(10)

  val run: Eval[(Total, Int)] = combined.run(zero)
  val runs: Eval[Total] = combined.runS(zero)
  val runa: Eval[Int] = combined.runA(zero)

  println(run.value) // (Total(23),43)
  println(runs.value)
  println(runa.value)

  /**
    * useful and convenient state manipulation functions:
    */
  // extract the state                 and return IT  as a value
  val st_get: State[Total, Total] = State.get[Total]
  // extract the state, apply FUNCTION and return NEW as a value
  val st_inspect: State[Total, String] = State.inspect[Total, String](s => s"${s.t} !")

  // modify the state with the NEW GIVEN              and return a unit as a value
  val st_set:    State[Total, Unit] = State.set[Total](Total(23))
  // modify the state by applying FUNCTION TO CURRENT and return a unit as a value
  val st_modify: State[Total, Unit] = State.modify[Total](s => Total(s.t*1000))

  // just lifting ANY value to ANY State monad, w/o touching the real state.
  val st_pure:   State[Total, String] = State.pure[Total, String]("42!")

  val combined2 = for {
    _ <- State.modify[Total] { s => Total(s.t + 10) }
    _ <- State.modify[Total] { s => Total(s.t + 2) }
    _ <- State.modify[Total] { s => Total(s.t + 1) }
    n <- State.inspect[Total, Int] { s => s.t }
  } yield n

  val re: (Total, Int) = combined2.run(Total(10)).value
  print(re)

}
