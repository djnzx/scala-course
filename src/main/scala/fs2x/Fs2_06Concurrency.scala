package fs2x

import fs2._
import cats.effect.IO
import cats.effect.{ContextShift, IO}

object Fs2_06Concurrency extends App {
  /**
    * .merge needs ContextShift to shift its calculation to another thread pool
     */
  implicit val ioContextShift: ContextShift[IO] =
    IO.contextShift(scala.concurrent.ExecutionContext.Implicits.global)

  Stream(1,2,3)
    .merge(Stream.eval(IO { Thread.sleep(200); 4 }))
    .compile
    .toVector
    .unsafeRunSync()

}
