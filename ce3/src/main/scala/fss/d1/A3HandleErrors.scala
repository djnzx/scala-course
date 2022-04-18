package fss.d1

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2._

object A3HandleErrors extends App {

  /** effectful error */
  val x0a: Stream[IO, INothing] = Stream.raiseError[IO](new Exception("oh noes!"))

  /** pure Stream[Int] */
  val se1: Stream[Pure, Int] = Stream(1, 2, 3)

  /** pure Stream[String] */
  val se2: Stream[Pure, String] = Stream("qq", "ww", "ee")

  /** pure Stream + pure Exception */
  lazy val ce0: Stream[Pure, Int] = se1 ++ (throw new Exception("!@#$")) ++ se1
  val h0: Stream[Pure, Int] = ce0.handleErrorWith(_ => Stream.emit(-1))

  /** plain exception */
//  println(ce0.toList)

  /** will stop after first error */
  println(h0.toList) // List(1, 2, 3, -1)

  /** combination pure + effectful lifts pure to effectful */
  val w1: Stream[IO, Int] = se1 ++ x0a ++ se1

  /** wrong error handling */
  val w2: Stream[IO, String] = (se2 ++ x0a ++ se2).handleErrorWith(_ => Stream.emit("too BAD"))
  println(w2.compile.toList.unsafeRunSync())

  val x0b: Stream[IO, Int] = x0a.handleErrorWith(_ => Stream.emit(-3))
  val x0c: Stream[IO, String] = x0a.handleErrorWith(_ => Stream.emit("too BAD"))

  /** proper error handling */
  val p1: Stream[IO, Int] = se1 ++ x0b ++ se1
  val p2: Stream[IO, String] = se2 ++ x0c ++ se2

  println(p1.compile.toList.unsafeRunSync())
  println(p2.compile.toList.unsafeRunSync())

  def logic(x: Int) = if (x % 2 == 0) IO(x) else IO.raiseError(new IllegalArgumentException(s"x=$x is odd!"))
  val data: Vector[IO[Int]] = (1 to 5).toVector.map(logic)

}
