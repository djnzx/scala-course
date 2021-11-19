package tinkoff

import scala.concurrent.Future
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global

/** На вход Seq[Future[String]] Получить Future[(Seq[String], Seq[Throwable]) - результат агрегации выполненых Future и
  * исключений
  */
object Tinkoff2 extends App {

  def repack(data: Seq[Either[Throwable, String]]): (Seq[String], Seq[Throwable]) =
    data.foldLeft((Seq.empty[String], Seq.empty[Throwable])) {
      case ((ss, ts), Left(t)) => (ss, t +: ts)
      case ((ss, ts), Right(s)) => (s +: ss, ts)
    }

  def sequence(xs: Seq[Future[String]]): Future[(Seq[String], Seq[Throwable])] =
    xs.traverse(_.map(Right(_)).recover(Left(_)))
      .map(repack)

  val data = Seq(
    Future.successful("a"),
    Future.successful("b"),
    Future.failed(new IllegalArgumentException("z")),
  )

  val r: Future[(Seq[String], Seq[Throwable])] = sequence(data)

  pprint.pprintln(r)

}
