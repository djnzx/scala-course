package fs2x.d11fanin

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Semaphore
import fs2._
import fs2.concurrent.Topic
import scala.concurrent.duration.DurationInt

/** EACH message consumed by EACH stream
  * to terminate, just close topic
  * all streams will be closed automatically
  */
object StreamFromTopic extends IOApp.Simple {
  import Core._

  override def run: IO[Unit] = for {
    sem <- Semaphore[IO](1)
    t   <- Topic[IO, String]
    s0 = data
           .covary[IO]
           .metered(1.second)
           .through(t.publish)
           .onFinalize(t.close.void)
    s1 = t.subscribeUnbounded
    s2 = t.subscribeUnbounded
    s   <- Stream(
             s0,
             s1.evalTap(s => print(sem, "S1" -> s)),
             s2.evalTap(s => print(sem, "S2" -> s)),
           ).parJoinUnbounded.compile.drain
  } yield s
}
