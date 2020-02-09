package aa_fp

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Fps072 extends App {

  val o1: Option[Int] = Some(1)

  val f1: Future[Option[Int]] = o1 match {
    case Some(v) => Future { Some(v) }
    case None => Future.successful(None)
  }

  val f2: Future[Option[Int]] = o1 match {
    case Some(v) => Future { Option(v) }
    case None    => Future.successful(Option.empty)
  }

  val f3: Future[Option[Int]] = if (o1.isEmpty) Future.successful(None) else Future { Option(o1.get) }
  val f4: Future[Option[Int]] = o1.fold(Future.successful(Option.empty[Int]))(x => Future { Some(x) })

}
