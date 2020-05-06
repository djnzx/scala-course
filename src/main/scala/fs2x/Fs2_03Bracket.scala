package fs2x

import java.util.concurrent.atomic.AtomicLong

import cats.effect.IO
import fs2.{Chunk, INothing, Pure, Stream}

object Fs2_03Bracket extends App {
  val err: Stream[IO, INothing] = Stream.raiseError[IO](new Exception("oh noes!"))
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
