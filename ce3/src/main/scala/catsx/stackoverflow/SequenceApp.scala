package catsx.stackoverflow

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SequenceApp extends App {

  def foo(x:String): Future[Seq[Int]] = Future { List(1,2,3) }
  val r: Future[Map[Int, Seq[Int]]] = foo("").map { e => e.groupBy(identity) }


  def getStrings(lst: Iterable[String]): Iterable[String] = {
    val someStrings = lst.filter(_.length >= 6)
    val stringCount = someStrings.foldLeft(0)((accum, _) => accum + 1)
    println(stringCount)
    someStrings
  }

  // with immutable List
  def foldAndFilter1[A, M](orig: Iterable[A])(p: A => Boolean)(mEmpty: M)(mf: (M, A) => M): (M, Iterable[A]) =
    orig.foldLeft((mEmpty, List.empty[A])) { case ((meta, filtered), item) =>
      (mf(meta, item), if (p(item)) item::filtered else filtered)
    } match { case (meta, list) => (meta, list.reverse) }

  // with mutable ListBuffer
  def foldAndFilter2[A, M](orig: Iterable[A])(p: A => Boolean)(mEmpty: M)(mf: (M, A) => M): (M, Iterable[A]) =
    orig.foldLeft((mEmpty, ListBuffer.empty[A])) { case ((meta, filtered), item) =>
      (mf(meta, item), if (p(item)) filtered:+item else filtered)
    }

  import cats.{Applicative, Monoid, Traverse}
  import cats.syntax.applicative._
  def foldAndFilter3[F[_]: Traverse: Applicative, A, M](orig: F[A])(p: A => Boolean)(mf: (M, A) => M)(implicit fam: Monoid[F[A]], mm: Monoid[M]): (M, F[A]) =
    Traverse[F].foldLeft(orig, (mm.empty, fam.empty)) { case ((meta, filtered), item) =>
      (mf(meta, item), if (p(item)) fam.combine(filtered, item.pure[F]) else filtered )
    }

  val rs1: (Int, Iterable[Int]) = foldAndFilter1(1 to 10 toList)(n => n%2 == 0)(0)((m, _) => m+1)
  val rs2: (Int, Iterable[Int]) = foldAndFilter2(1 to 10 toList)(n => n%2 == 0)(0)((m, _) => m+1)
  import cats.instances.list._
  import cats.instances.int._
  val rs3: (Int, Iterable[Int]) = foldAndFilter3(1 to 10 toList)(n => n%2 == 0)((m:Int, _) => m+1)
  println(rs1)
  println(rs2)
  println(rs3)

  val d1: List[Int] = 1 to 1000 toList
  val d2: Vector[Int] = 1 to 1000 toVector
  val l1 = d1.length // O(N)
  val l2 = d2.length // O(1)
  val el1 = d1(10) // d1.apply(10) // O(N)
  val el2 = d2(10) // d2.apply(10) // O(1)
  d1.grouped(100)
  d2.grouped(100)
}
