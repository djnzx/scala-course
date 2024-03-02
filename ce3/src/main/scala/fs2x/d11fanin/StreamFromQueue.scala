package fs2x.d11fanin

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Queue
import cats.effect.std.Semaphore
import fs2._
import scala.concurrent.duration.DurationInt

/** EACH message consumed by ONE (we don't know which) stream
  * to terminate, we need to know exact number of consumers
  * to send exactly None * number
  */
object StreamFromQueue extends IOApp.Simple {
  import Core._

  override def run: IO[Unit] = for {
    sem <- Semaphore[IO](1)
    q   <- Queue.unbounded[IO, Option[String]]
    // stream as a source
    s0 = (data.map(Some(_)) ++ Stream(None, None))
           .covary[IO]
           .metered(1.second)
           .evalTap(q.offer)
    // consumer 1
    s1 = Stream.fromQueueNoneTerminated(q)
    // consumer 2
    s2 = Stream.fromQueueNoneTerminated(q)
    s   <- Stream(
             s0,
             s1.evalTap(s => print(sem, "S1" -> s)),
             s2.evalTap(s => print(sem, "S2" -> s)),
           ).parJoinUnbounded.compile.drain
  } yield s

}
