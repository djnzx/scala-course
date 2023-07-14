package fs2x

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

object Fs2_04 extends App {
  val a: List[Int]       = Stream(1, 0).repeat.take(6).toList
  val b: List[Nothing]   = Stream(1, 2, 3).drain.toList
  val c: Vector[Nothing] = Stream.eval(IO(println("!!"))).drain.compile.toVector.unsafeRunSync()
  val d: List[Either[Throwable, Int]] =
    (
      Stream(1, 2) ++ Stream(3).map(_ => throw new Exception("nooo!!!")) ++ Stream(4)
      )
      .attempt.toList
  pprint.pprintln(d)
}
