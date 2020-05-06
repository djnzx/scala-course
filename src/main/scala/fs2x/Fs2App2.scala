package fs2x

import cats.effect.IO
import fs2.{Chunk, INothing, Pure, Stream}

object Fs2App2 extends App {
  val ch1: Chunk[Double] = Chunk.doubles(Array(1.0, 2.0, 3.0))
  val s1c: Stream[Pure, Double] = Stream.chunk(ch1)

  val s1d: Stream[Pure, Double] = s1c.mapChunks { dc =>
    val ds: Chunk.Doubles = dc.toDoubles
    ds
  }
  val sa = Stream(1,2,3) ++ Stream.emit(42)
  val sb: Stream[IO, Int] = Stream(1,2,3) ++ Stream.eval(IO.pure(4))
  val vb: Vector[Int] = sb.compile.toVector.unsafeRunSync()

  val err1: Stream[Pure, Int]    = Stream(1,2,3) ++ (throw new Exception("!@#$"))
  val err2: Stream[IO, INothing] = Stream.raiseError[IO](new Exception("oh noes!"))
  val err3: Stream[IO, Nothing]  = Stream.eval(IO(throw new Exception("error in effect!!!")))

  val err2a: List[String] = err2.handleErrorWith { e => Stream.emit(e.getMessage) }.compile.toList.unsafeRunSync()
  println(err2a)

}
