package fss1

import cats.effect.IO
import fs2.*

object FSS1 extends App {

  val x = Stream(1, 2, 3)
    .repeat
    .chunkN(2)
    .take(5)
    .toList

  val s0: Stream[IO, Int] = Stream.eval(IO(33))

  pprint.pprintln(x)
}
