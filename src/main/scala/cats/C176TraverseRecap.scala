package cats

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object C176TraverseRecap extends App {
  /**
    * resolving future and printing with prefix
    */
  def xo(prefix: String, x: Future[List[Int]]): Unit = {
    val r = Await.result(x, 1.seconds)
    println(s"$prefix: $r")
  }

  /**
    * basic problem:
    * we have List[A]
    * and we have A => F[B]
    * by doing map, we get `List[F[B]]`
    * but we want to have `F[List[B]]`
    */
  val names: List[String] = List(
    "site1.com",
    "site22.com",
    "site333.com",
  )
  val getUptime = (host: String) => Future { host.length*100 }
  /**
    * by doing map -> we get `List[Future[Int]]`
    */
  val upTimes: List[Future[Int]] = names map getUptime
  /**
    * solution 1.
    * we can fold it manually
    * but the problem is - we need to write a lot and be careful about `empty element`
    * in out case `Future { List.empty[Int] }`
    */
  val result1: Future[List[Int]] = upTimes.foldLeft(Future { List.empty[Int] })((facc, fitem) => for {
    acc <- facc
    item <- fitem
  } yield acc :+ item)
//  xo("S1", result1)

  /**
    * solution 1a.
    * we can extract empty element from cats
    */
  {
    import cats.instances.list._     // Monoid[List[Int]]
    import cats.instances.future._   // Applicative[Future]
    import cats.syntax.applicative._ // .pure syntax

    def empty: Future[List[Int]] = Monoid[List[Int]].empty.pure[Future]
    val result1a: Future[List[Int]] = upTimes.foldLeft(empty)((facc, fitem) => for {
      acc <- facc
      item <- fitem
    } yield acc :+ item)
//    xo("S1A", result1a)
  }

  /**
    * solution 1b.
    * let's make empty more generic
    */
  {
    import cats.instances.list._     // Monoid[List[Int]]
    import cats.instances.future._   // Applicative[Future]
    import cats.syntax.applicative._ // .pure syntax

    def empty[A]= Monoid[List[A]].empty.pure[Future]
    val result1b: Future[List[Int]] = upTimes.foldLeft(empty[Int])((facc, fitem) => for {
      acc <- facc
      item <- fitem
    } yield acc :+ item)
//    xo("S1B", result1b)
  }

  /**
    * solution 1c.
    * let's make empty more-more generic
    * right now we can fold over any F[_], not only Future[]
    */
  {
    import cats.instances.list._     // Monoid[List[Int]]
    import cats.instances.future._   // Applicative[Future]
    import cats.syntax.applicative._ // .pure syntax

    def empty[F[_]: Applicative, A]: F[List[A]] = Monoid[List[A]].empty.pure[F]
    val result1c: Future[List[Int]] = upTimes.foldLeft(empty[Future, Int])((facc, fitem) => for {
      acc <- facc
      item <- fitem
    } yield acc :+ item)
//    xo("S1C", result1c)
  }

  /**
    * solution 2.
    * we can use specific method from Future
    * but the problem is - it works only for futures
    */
  val result2: Future[List[Int]] = Future.traverse(names)(getUptime)
//  xo("S2", result2)

  /**
    * solution 3.
    * we can use Cat's traverse
    */
  {
    import cats.instances.list._
    import cats.instances.future._

    val result3: Future[List[Int]] = Traverse[List].traverse(names)(getUptime)
//    xo("S3", result3)
  }

  /**
    * solution 4.
    * by using Applicative
    */
  {
    import cats.instances.future._
    import cats.instances.list._
    import cats.syntax.applicative._ // pure
    import cats.syntax.apply._       // mapN

    def empty0[B]                   : Future[List[B]] = List()               .pure[Future]
    def empty1[B]                   : Future[List[B]] = List.empty[B]        .pure[Future]
    def empty2[B]                   : Future[List[B]] = Monoid[List[B]].empty.pure[Future]
    def empty3[F[_]: Applicative, B]:      F[List[B]] = Monoid[List[B]].empty.pure[F]

    val combine1: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
      (facc: Future[List[Int]], fitem: Future[Int]) => for {
        acc <- facc
        item <- fitem
      } yield acc :+ item

    val combine2: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
      (facc: Future[List[Int]], fitem: Future[Int]) =>
        Semigroupal.map2(facc, fitem)(_ :+ _)

    val combine3: (Future[List[Int]], Future[Int]) => Future[List[Int]] =
      (facc: Future[List[Int]], fitem: Future[Int]) =>
        (facc, fitem).mapN(_ :+ _)

    def combine4[B](facc: Future[List[B]], fitem: Future[B]): Future[List[B]] =
        (facc, fitem).mapN(_ :+ _)

    def combine5[F[_]: Applicative, B](facc: F[List[B]], fitem: F[B]): F[List[B]] =
        (facc, fitem).mapN(_ :+ _)

    val result4a: Future[List[Int]] = upTimes.foldLeft(empty3[Future, Int])(combine1)
    val result4b: Future[List[Int]] = upTimes.foldLeft(empty3[Future, Int])(combine2)
    val result4c: Future[List[Int]] = upTimes.foldLeft(empty3[Future, Int])(combine3)
    xo("S4C", result4c)

    // traverse reinvention
    def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(empty3[F, B])((as, a) => combine5(as, func(a)))

    val result5a: Future[List[Int]] = listTraverse(names)(getUptime)

    def listSequence1[F[_]: Applicative, B] (list: List[F[B]]): F[List[B]] =
      list.foldLeft(empty3[F, B])((as, a) => combine5(as, a))

    def listSequence2[F[_]: Applicative, B] (list: List[F[B]]): F[List[B]] =
      listTraverse(list)(identity)

  }


}
