package topics.monad_transform.v1_hard_coded_for

import topics.monad_transform.monads.{IO, Monad, StateT}

object StateTHardCodedForExpression extends App {

  implicit val IOMonad: Monad[IO] = new Monad[IO] {
    def lift[A](a: => A): IO[A] = IO(a)
    def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] = ma.flatMap(f)
  }

  case class IntState(i: Int)

  def add(i: Int) = StateT[IO, IntState, Int] { st: IntState =>
    val newValue = st.i + i
    val newState: IntState = st.copy(i = newValue)
    val r: (IntState, Int) = (newState, newValue)
    IO(r)
  }

  def mult(i: Int) = StateT[IO, IntState, Int] { st: IntState =>
    val newValue = st.i * i
    val newState = st.copy(i = newValue)
    val r: (IntState, Int) = (newState, newValue)
    IO(r)
  }

  val a: StateT[IO, IntState, Int] = add(1)       // StateT[IO, IntState, Int]
  val b: IO[(IntState, Int)] = a.run(IntState(1)) // IO[(IntState, Int)]
  b.map(t => println(s"b state = ${t._1}"))       // prints “b state = IntState(2)”

  // a hard-coded example
  val forExpression: StateT[IO, IntState, Int] = for {
    _ <- add(2)    //3
    _ <- add(3)    //6
    x <- mult(10)  //6x10
  } yield x

  // “run” the state, v1
  val result: IO[(IntState, Int)] = forExpression.run(IntState(1))
  var r = result.run

  // print the final state
  result.map(tuple => println(s"IntState = ${tuple._1}"))
  println(s"r = ${r}")
}
