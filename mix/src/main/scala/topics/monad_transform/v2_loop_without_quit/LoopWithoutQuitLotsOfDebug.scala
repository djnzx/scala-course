package topics.monad_transform.v2_loop_without_quit

import topics.monad_transform.monads.{IO, Monad, StateT}

object LoopWithoutQuitLotsOfDebug extends App {

    // the `IO` functions i used previously
    def getLine(): IO[String] = IO {
        val in = scala.io.StdIn.readLine()
        //println(s"getLine: in = |$in|")
        in
    }

    def putStr(s: String): IO[Unit] = IO {
        //println(s"putStr: s = |$s|")
        print(s)
    }

    def toInt(s: String): Int = {
        //println(s"toInt: s = $s")
        try {
            s.toInt
        } catch {
            case e: NumberFormatException => 0
        }
    }

    // a class to track the sum of the ints that are given
    case class Sum(value: Int)

    // an implementation of the `Monad` trait for the `IO` type
    implicit val IOMonad: Monad[IO] = new Monad[IO] {
        def lift[A](a: => A): IO[A] = {
            // only see this output when the loop exits properly
            //println(s"IOMonad::point received 'a': $a")
            IO(a)
        }
        def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] = ma.flatMap(f)
    }

    /**
      * given the int `delta`, add it to the previous `sum` from the given SumState `s`;
      * then return a new state `newState`, created with the new sum;
      * at the end of the function, wrap `newState` in an `IO`;
      * the anonymous function creates a `StateT` wrapped around that `IO`.
      */
    def doSumWithStateT(delta: Int): StateT[IO, Sum, Int] = StateT { st: Sum =>

        // create a new sum from `i` and the previous sum from `s`
        val value2 = st.value + delta
        println(s"updateIntState, old sum:   " + st.value)
        println(s"updateIntState, new input: " + delta)
        println(s"updateIntState, new sum:   " + value2)

        // create a new SumState
        val state2: Sum = st.copy(value = value2)

        // return the new state and the new sum, wrapped in an IO
        IO(state2, value2)
    }

    /**
      * the purpose of this function is to “lift” an IO action into the StateT monad.
      * given an IO instance named `io` as input, the anonymous function transforms
      * the `IO[A]` into an `IO[(SumState, A)]`;
      * that result is then wrapped in a `StateT`.
      */
    def liftIoIntoStateT[A](io: IO[A]): StateT[IO, Sum, A] = StateT { st: Sum =>

        // transform `IO[A]` into `IO(SumState, A)`
        val result: IO[(Sum, A)] = io.map { a => (st, a) }

        // debug: use this as a way to see what's going on here. if you enter 1 and then 2
        // you'll see the output, `(SumState(1), 2)`.
        //result.map(tup => println(s"lift: (${tup._1}, ${tup._2})"))

        // yield the result of this anonymous function (which will be wrapped by StateT)
        result
    }

    // new versions of the i/o functions that uses StateT
    def getLineAsStateT():         StateT[IO, Sum, String] = liftIoIntoStateT(getLine)
    def putStrAsStateT(s: String): StateT[IO, Sum, Unit]   = liftIoIntoStateT(putStr(s))

    /**
      * you have to kill this loop manually (i.e., CTRL-C)
      */
    def sumLoop: StateT[IO, Sum, Unit] = for {
        _     <- putStrAsStateT("\ngive me an int: ")
        input <- getLineAsStateT
        i     <- liftIoIntoStateT(IO(toInt(input)))
        _     <- doSumWithStateT(i)
        _     <- sumLoop
    } yield ()

    val result: (Sum, Unit) = sumLoop.run(Sum(0)).run

    // this line won't be reached because you have to kill the loop manually
    println(s"Final SumState: ${result._1}")

}



