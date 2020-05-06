package fs2x

import cats.effect.{IO, Sync}
import fs2._

object Fs2_08SyncEff extends App {

  def destroyUniverse(): Unit = { println("BOOOOM!!!"); }

  val s1: Stream[IO, INothing] = Stream.eval_(IO { destroyUniverse() })
  val s2: Stream[Pure, String] = Stream("...moving on")
  val s:  Stream[IO, String]   = s1 ++ s2
  // s: Stream[IO[x], String] = Stream(..)
  val r: Vector[String] = s.compile.toVector.unsafeRunSync()
  println(r)

  val T: Sync[IO] = Sync[IO]
  // T: Sync[IO] = cats.effect.IOInstances$$anon$3@4f39f63c
  val s1a: Stream[IO, INothing] = Stream.eval_(T.delay { destroyUniverse() })
  val s4: Stream[IO, String] = s1a ++ s2
  // s3: Stream[IO[x], String] = Stream(..)
  val r2: Seq[String] = s4.compile.toVector.unsafeRunSync()
  println(r2)

}
