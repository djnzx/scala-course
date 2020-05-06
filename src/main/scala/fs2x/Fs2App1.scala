package fs2x

import fs2.{INothing, Pure, Stream}
import cats.effect.IO

object Fs2App1 extends App {
  // initiating
  val s0: Stream[Pure, INothing] = Stream.empty
  val s1: Stream[Pure, Int] = Stream.emit(1)
  val s1a: Stream[Pure, Int] = Stream(1,2,3)
  val s1b: Stream[Pure, Int] = Stream.emits(List(1,2,3))

  // terminating
  val l1a: List[Int] = s1a.toList
  val l1b: List[Int] = s1b.toList

  val a: List[Int] = (Stream(1,2,3) ++ Stream(4,5)).toList
  Stream(1,2,3).map(_ + 1)
  Stream(1,2,3).filter(_ % 2 != 0)
  val s2: Stream[Pure, Int] = Stream(1,2,3).fold(0)(_ + _) // Stream of one element
  val s3: Stream[Pure, Int] = Stream(None,Some(2),Some(3)).collect { case Some(i) => i }
  // insert after each
  val s4: Stream[Pure, Int] = Stream.range(0,5).intersperse(42)

  val s5 = Stream(1,2,3).flatMap(i => Stream(i,i)) // (1, 1, 2, 2, 3, 3)
  val s6 = Stream(1,2,3).repeat.take(9) // repeat infinitely, and take 9: (1, 2, 3, 1, 2, 3, 1, 2, 3)
  val s7 = Stream(1,2,3).repeatN(2) // (1, 2, 3, 1, 2, 3)
  val s8 = Stream(1,2,3).flatMap(n => Stream(n).repeatN(n)) // (1, 2, 2, 3, 3, 3)

  val eff1: Stream[IO, Int] = Stream.eval(IO { println("BEING RUN!!"); 1 + 1 }).repeatN(2)

  // just run, and throw away the result
  val sa: Unit = eff1.compile
    .drain
    .unsafeRunSync()
//  println("--")

  // run and collect the data to collection
  val sb: Vector[Int] = eff1.compile
    .toVector
    .unsafeRunSync()
//  println(sb)
//  println("--")

  // run and fold
  val sc: Int = eff1.compile
    .fold(0) { _ + _}
    .unsafeRunSync()
//  println(sc)

}
