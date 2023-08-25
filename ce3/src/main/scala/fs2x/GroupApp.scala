package fs2x

import cats.Eq
import cats.effect.IO
import cats.effect.IOApp
import fs2._

object GroupApp extends IOApp.Simple {

  implicit class StreamUnpackTupleOps[A, F[_]](sa: Stream[F, A]) {
    def regroupAfterJoin[B: Eq, C](f: A => B)(g: A => C): Stream[F, (B, List[C])] =
      sa.groupAdjacentBy(f)
        .map { case (k, row) => k -> row.map(g).toList }
  }

  val app =
    fs2
      .Stream(
        (1, "A"),
        (1, "B"),
        (2, "Q"),
        (2, "W"),
        (2, "E")
      )
      .regroupAfterJoin(_._1)(_._2)
      .covary[IO].evalTap(x => IO(pprint.pprintln(x)))
      .compile
      .drain

  override def run: IO[Unit] = app
}
