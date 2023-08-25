package slickvt

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import cats.implicits.{catsSyntaxOptionId, none}

object RegroupApp extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val data = Source(Seq(
    (1, "A"),
    (1, "B"),
    (2, "Q"),
    (2, "W"),
    (2, "E")
  ))

  def addEnclosingElement[A, M](src: Source[A, M]): Source[Option[A], M] = src.map(_.some) ++ Source.single(None)

  def regroupAfterJoin[A, L, R](lf: A => L)(rf: A => R): Flow[Option[A], (L, Seq[R]), NotUsed] = Flow[Option[A]]
    .scan(
      (none[L], Seq.empty[R], none[(L, Seq[R])])
    ) {
      // first - setup
      case ((None, _, _), Some(a)) => (lf(a).some, Seq(rf(a)), None)
      // next - same element - keep collecting
      case ((Some(l), rs, _), Some(a)) if lf(a) == l => (l.some, rs :+ rf(a), None)
      // next - different - EMIT
      case ((l, rs, _), Some(a)) => (lf(a).some, Seq(rf(a)), l.map(_ -> rs))
      // last - state empty - ignore
      case (s@(_, Seq(), _), None) => s
      // last - non empty - EMIT
      case ((l, rs, _), None) => (None, Seq.empty, l.map(_ -> rs))
    }
    .mapConcat { case (_, _, next) => next }

  def process[A, L, R](src: Source[A, NotUsed])(l: A => L)(r: A => R)(fn: ((L, Seq[R])) => Unit) =
    addEnclosingElement(src)
      .via(regroupAfterJoin(l)(r))
      .runForeach(fn)

  def print(x: Any) = println(x)

  process(data)(_._1)(_._2)(print)


  system.terminate()

}
