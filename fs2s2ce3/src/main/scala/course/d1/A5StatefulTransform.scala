package course.d1

//import cats._
//import cats.implicits._
import cats.effect.std.Random
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.toTraverseOps
import fs2._

/**   - https://blog.kebab-ca.se/chapters/fs2/pulls.html
  *   - https://gist.github.com/dsebban/dbe7c8df5e31dead36e0dda1f0108714
  */
object A5StatefulTransform extends IOApp.Simple {

  val random: IO[Random[IO]] = Random.scalaUtilRandom[IO]

  val randoms: IO[Vector[Int]] = (1 to 20).map(_ => random.flatMap(_.betweenInt(1, 11))).toVector.sequence
  val app1 = randoms.flatMap(xs => IO(pprint.pprintln(xs)))

  /** take N elements only */
  def tk[F[_], O](n: Long): Pipe[F, O, O] = (in: Stream[F, O]) =>
    in.scanChunksOpt(n) { st: Long =>
      Option.when(st > 0) { chIn: Chunk[O] =>
        chIn.size match {
          case m if m < st => (st - m, chIn)
          case _           => (0, chIn.take(st.toInt))
        }
      }
    }

  /** group by N */
  def gb[F[_], O](n: Int) = (in: Stream[F, O]) =>
    in.repeatPull { pull: Stream.ToPull[F, O] =>
      pull.unconsN(n, true).flatMap {
        //                          values      state
        case Some((hd, tl)) => Pull.output1(hd).as(Some(tl))
        case None           => Pull.pure(None)
      }
    }

  /** group by N pull 1by1 based */
  def gbm[F[_], O](n: Int)(implicit no: Numeric[O]): Stream[F, O] => Stream[F, List[O]] = {

    // by N items
    def processV1(buf: List[O], sIn: Stream[F, O]): Pull[F, List[O], Unit] =
      sIn.pull.uncons1.flatMap {
        /** buffer is ready, emit buffer and start collecting again */
        case Some((element, tail)) if buf.size == n => Pull.output1(buf) >> processV1(List(element), tail)
        /** buffer is not ready, keep collecting */
        case Some((element, tail)) => processV1(buf :+ element, tail)
        /** no more items, but buffer is not empty */
        case None if buf.nonEmpty => Pull.output1(buf)
        /** no more items, buffer is empty */
        case None => Pull.done
      }

    // sum is no more than N
    def processV2(buf: List[O], sIn: Stream[F, O]): Pull[F, List[O], Unit] =
      sIn.pull.uncons1.flatMap { // here we match Option[(O, Stream[O])]
        /** buffer is ready, emit buffer and start collecting again */
        case Some((x, tail)) if no.toInt(buf.sum) + no.toInt(x) > n => Pull.output1(buf) >> processV2(List(x), tail)
        /** buffer is not ready, keep collecting */
        case Some((x, tail)) => processV2(buf :+ x, tail)
        /** no more items, but buffer is not empty */
        case None if buf.nonEmpty => Pull.output1(buf)
        /** no more items, buffer is empty */
        case None => Pull.done
      }

    in: Stream[F, O] => processV2(List.empty, in).stream
  }

  val app2 = Stream
    .evals(randoms)
    .through(gbm(17))
    .compile
    .toList
    .flatTap(xs => IO(pprint.pprintln(xs)))

  override def run: IO[Unit] = app2.void

}
