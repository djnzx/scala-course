package guillaumebogard

import cats.Applicative
import cats.data.Nested
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ApplicativeApp2 extends App {

  val x: Future[Option[Int]] = Future.successful(Some(5))
  val y: Future[Option[Char]] = Future.successful(Some('a'))

  val composed: Future[Option[Int]] =
    Applicative[Future].compose[Option].map2(x, y)(_ + _)

  val nested: Nested[Future, Option, Int] =
    Applicative[Nested[Future, Option, *]].map2(Nested(x), Nested(y))(_ + _)

}
