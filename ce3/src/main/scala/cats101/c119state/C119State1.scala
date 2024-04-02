package cats101.c119state

import cats.data.State
import cats.implicits.catsSyntaxApplicativeId

object C119State1 {

  /** state */
  case class Total(t: Int)

  val addN = (n: Int) => State[Total, Int] { s =>
    val r = s.t + n
    (Total(r), r)
  }

  val mulN = (n: Int) => State[Total, Int] { s =>
    val r = s.t * n
    (Total(r), r)
  }

  val add2: State[Total, Int] = addN(2)
  val mul2: State[Total, Int] = mulN(2)

  /** you can grab intermediate states, and still construct the combined function */
  val combined2a: State[Total, (Int, Int, Int)] = for {
    a <- addN(10) // + 10 / a = 100 + 10 = 110
    b <- mul2     // *2   / b = 110 * 2  = 220
    c <- add2     // +1   / c = 220 + 2  = 222
  } yield (a, b, c)

  type MState[A] = State[Total, A]
  val combined2b: State[Total, (Int, Int, Int)] =
    addN(10).flatMap { a =>
      mul2.flatMap { b =>
        add2.flatMap { c =>
          (a, b, c).pure[MState]
        }
      }
    }

  val combined2c: State[Total, (Int, Int, Int)] =
    addN(10).flatMap { a =>
      mul2.flatMap { b =>
        add2.map { c =>
          (a, b, c)
        }
      }
    }

  val r = combined2a.run(Total(100)).value
  println(r)

}
