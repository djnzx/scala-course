package fs2x

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Stream

import java.util.concurrent.atomic.AtomicLong

object Fs2_03Bracket extends App {
  val err: Stream[IO, Nothing] = Stream.raiseError[IO](new Exception("oh noes!"))
  // Resource acquisition
  val count: AtomicLong = new java.util.concurrent.atomic.AtomicLong(0)
  val acquire: IO[Unit] = IO { println("incremented: " + count.incrementAndGet); () }
  val release: IO[Unit] = IO { println("decremented: " + count.decrementAndGet); () }

  Stream
    // bracket will work
    // even internal part fails
    .bracket(acquire)(_ => release)
    .flatMap(_ => Stream(1,2,3) ++ err)
    .compile
    .drain
    .unsafeRunSync()

  println(count.get)

}
