package fs2x

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2._

object Fs2_06Concurrency extends App {
  /**
    * .merge needs ContextShift to shift its calculation to another thread pool
     */
  Stream(1,2,3)
    .merge(Stream.eval(IO { Thread.sleep(200); 4 }))
    .compile
    .toVector
    .unsafeRunSync()

}
