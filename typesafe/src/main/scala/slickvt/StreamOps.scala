package slickvt

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxOptionId, none}

// TODO: move it to the library
trait StreamOps {

  def addEnclosingElement[A, M](src: Source[A, M]): Source[Option[A], M] =
    src.map(_.some) ++ Source.single(None)

  def regroupSequentialOptionAfterJoin[A, L, R](lf: A => L)(rf: A => R): Flow[Option[A], (L, Seq[R]), NotUsed] = Flow[Option[A]]
    .scan(
      (none[L], List.empty[R], none[(L, Seq[R])])
    ) {
      // first element - start collecting
      case ((None, _, _), Some(a)) => (lf(a).some, rf(a).pure[List], None)
      // next - same element - keep collecting
      case ((Some(l), rs, _), Some(a)) if lf(a) == l => (l.some, rf(a) :: rs, None)
      // next - different - EMIT
      case ((l, rs, _), Some(a)) => (lf(a).some, rf(a).pure[List], l.map(_ -> rs.reverse))
      // last - state empty - discard
      case (s@(_, Seq(), _), None) => s
      // last - non empty - EMIT
      case ((l, rs, _), None) => (None, List.empty, l.map(_ -> rs.reverse))
    }
    .mapConcat { case (_, _, next) => next }

  def regroupSequentialAfterJoin[A, L, R](src: Source[A, NotUsed])(l: A => L)(r: A => R): Source[(L, Seq[R]), NotUsed] =
    addEnclosingElement(src)
      .via(regroupSequentialOptionAfterJoin(l)(r))

}
