package doobiex

import cats.Foldable
import cats.data.NonEmptyList
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util
import doobie.util.fragment.Fragment
import doobie.util.fragments
import doobie.util.fragments._
import doobie.util.meta.Meta.IntMeta

object Doobie7whereIn extends IOApp.Simple {

  val printEmpty = IO(println("-= empty =-"))
  def printAll[F[_]: Foldable](xs: F[_]) = xs.traverse_(x => IO(println(x)))

  /** constructs Option[Fragment] for clause WHERE IN (?, ?, ?) */
  def whereInOpt[A: util.Put](field: String, values: List[A]): Option[doobie.Fragment] =
    values.toNel.map(nel => whereAnd(fragments.in(Fragment.const(field), nel)))

  /** constructs {{{ConnectionIO[List[R]]}}}
    * if second is None => fallback to {{{ConnectionIO[List.empty[R]]}}}
    * critical to use when WHERE id IN (...) has empty collection of values
    */
  def whereIn[R: Read](first: doobie.Fragment, second: Option[doobie.Fragment]) =
    (first.some, second)
      .mapN(_ ++ _)
      .map(_.query[R].to[List])
      .getOrElse(List.empty[R].pure[ConnectionIO])

  val f1 = whereIn[(Int, String)](fr"SELECT id, name FROM t1", whereInOpt("id", List()))
  val f2 = whereIn[(Int, String)](fr"SELECT id, name FROM t1", whereInOpt("id", List(1, 2, 3)))

  val data1: IO[List[(Int, String)]] = f1.transact(xa[IO])
  val data2: IO[List[(Int, String)]] = f2.transact(xa[IO])

  override def run: IO[Unit] = {
    data1.flatMap {
      case xs if xs.isEmpty => printEmpty
      case xs               => printAll(xs)
    } >>
      data2.flatMap {
        case xs if xs.isEmpty => printEmpty
        case xs               => printAll(xs)
      }.void
  }

}
