package fs2x.d11fanin

import cats.effect.IO
import cats.effect.std.Semaphore
import fs2._

object Core {

  val data = Stream
    .emits(LazyList.from(1).take(5))
    .map(_.toString)

  /** we use semaphore just not to break output */
  def print[A](sem: Semaphore[IO], x: A) = for {
    _ <- sem.acquire
    _ <- IO(pprint.pprintln(x))
    _ <- sem.release
  } yield ()

}
