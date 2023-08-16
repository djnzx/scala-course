package fs2x

import cats.effect._
import fs2._

object ContextX extends IOApp.Simple {

  val s: Stream[Pure, Int] = Stream(10, 11, 12)

  val pipe: Pipe[Pure, Int, String] =
    (s: Stream[Pure, Int]) => s.flatMap(x => Stream.emits(Seq.fill(x % 3 + 1)(x.toString)))

  def compose[A, B, Ctx](
      s: Stream[Pure, (A, Ctx)],
      ab: Pipe[Pure, A, B]
    ): Stream[Pure, (B, Ctx)] =
    s.flatMap { case (a, c) => Stream(a).through(ab).map(b => (b, c)) }

  val s2: Stream[Pure, (Int, Double)]    = s.map(x => x -> (x + 100).toDouble)
  val s3: Stream[Pure, (String, Double)] = compose(s2, pipe)

  override def run: IO[Unit] =
    s3
      .covary[IO]
      .evalTap(x => IO(pprint.pprintln(x)))
      .compile
      .drain

}
