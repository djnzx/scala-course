package cats101

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.list._
import cats.instances.future._
import cats.Monoid
import cats.syntax.applicative._

object C175Instances extends App {
  val fli1: Future[List[Int]] = Future { List.empty[Int] }
  val fli2: Future[List[Int]] = List.empty[Int].pure[Future]
  val fli3: Future[List[Int]] = Monoid[List[Int]].empty.pure[Future]
}
