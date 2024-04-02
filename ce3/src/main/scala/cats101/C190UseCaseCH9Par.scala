package cats101

import cats.Monoid

import scala.concurrent.{Await, Future}

object C190UseCaseCH9Par extends App {

  import C189UseCaseCH9Par.foldMap

  // original data
  val data : Vector[Int] = (1 to 200).toVector
  // split data
  val data1: Vector[Int] = (1 to 100).toVector
  val data2: Vector[Int] = (101 to 200).toVector

  import cats.instances.int._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  // parallel computation
  val f1: Future[Int] = Future { foldMap(data1)(identity) }
  val f2: Future[Int] = Future { foldMap(data2)(identity) }

  // combine #1
  val f3a1: Future[(Int, Int)] = for {
    r1 <- f1
    r2 <- f2
  } yield (r1, r2)
  val f3a2 = f3a1.map { case (a, b) => Monoid[Int].combine(a, b) }
  val f3a3: Int = Await.result(f3a2, 1.seconds)

  // combine #2
  val f3b1: Future[Int] = for {
    r1 <- f1
    r2 <- f2
  } yield Monoid.combine(r1, r2)
  val f3b2: Int = Await.result(f3b1, 1.seconds)

  // combine #3
  import cats.instances.vector._
  import cats.instances.future._
  import cats.syntax.traverse._

  val f3c1: Future[Vector[Int]] = Vector(f1, f2).sequence
  val f3c2: Vector[Int] = Await.result(f3c1, 1.seconds)
  val f3c3 = foldMap(f3c2)(identity)

  // combine #4
  val f3d1: Future[Int] = Monoid[Future[Int]].combine(f1, f2)
  val f3d2 = Await.result(f3d1, 1.seconds)

  // reference
  println(data.toList.sum) // 20100
  // way 1
  println(f3a3)
  // way 2
  println(f3b2)
  // way 3
  println(f3c3)
  // way 4
  println(f3d2)
}
