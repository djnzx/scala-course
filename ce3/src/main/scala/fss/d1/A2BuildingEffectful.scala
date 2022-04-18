package fss.d1

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Resource
import fs2._

object A2BuildingEffectful extends IOApp.Simple {

  /** effectful */
  val s1: Stream[IO, Int] = Stream.eval(IO { println("BEING RUN!!"); 1 + 1 })

  /** effectful can't be calculated toLis, toVector, etc, NEEDS TO BE COMPILED */
  val s1b: Stream.CompileOps[IO, IO, Int] = s1.compile

  val app = for {
    x <- s1b.toList // actually run the stream
    _ <- IO { pprint.pprintln(x) } // access the result and print it
  } yield ()

  val data: Seq[Int] = List(
    { println("a"); 1 },
    { println("b"); 2 },
    { println("c"); 3 }
  )

  val s2: Stream[IO, Int] = Stream.evals(IO(data))
  val s2a: Stream.CompileOps[IO, IO, Int] = s2.compile
  val s2b: IO[List[Int]] = s2a.toList // run stream but discard evaluated values

  val app3 = for {
    x <- s2a.toList // actually run the stream
    _ <- IO { pprint.pprintln(x) } // access the result and print it
  } yield ()

  val app4 = for {
    _ <- s2a.drain // run and discard the values
  } yield ()

  val app5 = for {
    x <- s2a.count // run
    _ <- IO { pprint.pprintln(x) } // access the result and print it
  } yield ()

  val app6 = for {
    x <- s2a.fold(0L)(_ + _)
    _ <- IO { pprint.pprintln(x) } // access the result and print it
  } yield ()

  /** wrap computation to the resource */
  val res: Resource[IO, List[Int]] = s2a.resource.toList

  /** stateful stream after compilation can be:
    *   - drain - RUN + DISCARD VALUE
    *   - toList - RUN + OBTAIN VALUES
    *   - fold - RUN + FOLD OBTAINED VALUES
    */

  override def run: IO[Unit] = app6

  val chunk: Chunk[Int] = Chunk.array(Array(10, 11, 12))
  val stream: Stream[Pure, Int] = Stream.chunk(chunk)

}
