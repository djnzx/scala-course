package catsx.c047functors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object C047Functors extends App {
  // types can be different
  val functor: Int => String = (n: Int) => n.toString
  // types must be the same
  val endofunctor: Int => Int = (n: Int) => n + 1

  List(1, 2, 3).map(n => n + 1)
  // value different, but structure - the same!
  val l2: Seq[String] = List(1, 2, 3).map(functor)
  val l3: Seq[Int] = List(1, 2, 3).map(endofunctor)

  val future: Future[String] =
    Future(123)
      .map(n => n + 1) // 124
      .map(n => n * 2) // 248
      .map(n => n + "!") // 248!

  val s: String = Await.result(future, 1.second)
}
