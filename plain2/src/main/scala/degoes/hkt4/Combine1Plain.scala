package degoes.hkt4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Combine1Plain extends App {

  // implementation #1: (List, List)
  def combine[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  // implementation #2: (Option, Option)
  def combine[A, B](optA: Option[A], optB: Option[B]): Option[(A, B)] =
    for {
      a <- optA
      b <- optB
    } yield (a, b)

  // implementation #3: (Future, Future)
  def combine[A, B](futA: Future[A], futB: Future[B]): Future[(A, B)] =
    for {
      a <- futA
      b <- futB
    } yield (a, b)

  // usage: each call - call of different function
  // problem: code duplication
  println(combine(List(1,2,3), List("a", "b", "c"))) // line 10
  println(combine(Some(2), Some("scala"))) // line 17
  println(Await.result(combine(Future(1000), Future(2000)), 4.seconds)) // line 24
}
