package fs2x

import fs2.{INothing, Pure, Stream}
import cats.effect.IO
import cats.effect.unsafe.implicits.global

object Fs2_01Basics extends App {
  // initiating
  val s0: Stream[Pure, Nothing] = Stream.empty
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

  // just run, and throw away the result O
  val sa1: Stream.CompileOps[IO, IO, Int] = eff1.compile
  val sa2: IO[Unit] = sa1.drain
  val sa3: Unit = sa2.unsafeRunSync()
//  println("--")

  // run ALL F[_] and collect the data O to collection
  val sb1: Stream.CompileOps[IO, IO, Int] = eff1.compile
  val sb2: IO[Vector[Int]] = sb1.toVector
  val sb3: Vector[Int] = sb2.unsafeRunSync()
//  println(sb)
//  println("--")

  // run ALL F[_] and fold O
  val sc1: Stream.CompileOps[IO, IO, Int] = eff1.compile
  val sc2: IO[Int] = sc1.fold(0) { _ + _}
  val sc3: Int = sc2.unsafeRunSync()
//  println(sc)

}
