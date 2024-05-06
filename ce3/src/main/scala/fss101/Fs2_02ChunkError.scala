package fs2x

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.{Chunk, INothing, Pure, Stream}

object Fs2_02ChunkError extends App {
  // stream is built from chunks
  val ch1: Chunk[Double] = Chunk.array(Array(1.0, 2.0, 3.0))
  val s1c: Stream[Pure, Double] = Stream.chunk(ch1)

  val sa = Stream(1,2,3) ++ Stream.emit(42)
  val sb: Stream[IO, Int] = Stream(1,2,3) ++ Stream.eval(IO.pure(4))
  val vb: Vector[Int] = sb.compile.toVector.unsafeRunSync()

  val err1: Stream[Pure, Int]    = Stream(1,2,3) ++ (throw new Exception("!@#$"))
  val err2: Stream[IO, INothing] = Stream.raiseError[IO](new Exception("oh noes!"))
  val err3: Stream[IO, Nothing]  = Stream.eval(IO(throw new Exception("error in effect!!!")))

  println("1--")
  val err1a: Any = try
    err1.toList
  catch {
    case e: Exception => println(e) // java.lang.Exception: !@#$
  }
  println("--")
  println(err1a)                    // Unit
  println("--1")

  println("2--")
  val err2a: List[String] = err2.handleErrorWith { e => Stream.emit(e.getMessage) }.compile.toList.unsafeRunSync()
  println("--")
  println(err2a)                    // List(oh noes!)
  println("--2")
  println("3--")
  val err3a: Unit = try
  err3.compile.drain.unsafeRunSync()
  catch {
    case e: Exception => println(e) // java.lang.Exception: error in effect!!!
  }
  println("--")
  println(err3a)                    // Unit
  println("--3")



}
