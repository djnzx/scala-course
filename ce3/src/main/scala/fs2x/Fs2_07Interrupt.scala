package fs2x

import cats.effect.unsafe.implicits.global
import cats.effect.{Deferred, IO}
import fs2.Stream

import scala.concurrent.duration._

object Fs2_07Interrupt extends App {

  val wholeProgram: Stream[IO, Unit] =
    Stream.eval(Deferred[IO, Unit]).flatMap { switch =>

      val printNow: IO[Unit] = IO(println(java.time.LocalTime.now))
      val terminate = switch.complete(())

      val switcher =
        Stream.eval(terminate).delayBy(5.seconds)

      val main: Stream[IO, Unit] =
        Stream.repeatEval(printNow).metered(1.second)

      main
        .interruptWhen(switch.get.attempt)
        .concurrently(switcher)
    }

  wholeProgram.compile.drain.unsafeRunSync
}
