package mortals

import scalaz._
import Scalaz._

/**
  * FP:
  * - total
  * - deterministic
  * - no direct interop (side effects)
  * - no exception
  * - no mutable state
  * - manual/tail recursion
  */
object Mortals001b {
  final class IO[A](val interpret: () => A) {
    def map[B](f: A => B): IO[B] = IO(f(interpret()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(f(interpret()).interpret())
  }
  object IO {
    def apply[A](a: =>A): IO[A] = new IO(() => a)
  }

  /**
    * fundamental thing:
    * IO(println(123)) === IO { println(123); ... }
    * just a syntax for:
    * holding value () => { println(123); } in a case class
    */
  // creation of definition, description !!!
  val p123 = IO(println(123))
  // running, is running in the main, in the end of the world
  p123.interpret
}
