package fss.d6unfold

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import cats.implicits.catsSyntaxOptionId
import scala.concurrent.duration.DurationInt
import scala.util.Random

object D6UnfoldExperiments extends IOApp.Simple {

  /** based on the state, we know, that's NO MORE ELEMENTS */
  trait Unfold[O, S] {
    val s0: S
    def next(s: S): Option[(O, S)]
  }
  object Unfold {
    def stop: Option[Nothing] = none
    def next[O, S](o: O, s: S): Option[(O, S)] = (o, s).some
  }

  /** based on the state, we know, that WE PRODUCE THE LAST ELEMENT */
  trait UnfoldLoop[O, S] {
    val s0: S
    def next(s: S): (O, Option[S])
  }
  object UnfoldLoop {
    def last[O](o: O): (O, Option[Nothing]) = (o, none)
    def next[O, S](o: O, s: S): (O, Option[S]) = (o, s.some)
  }

  /** 0,1,2,3,4 - like while, we know, that NO MORE ELEMENTS */
  object FiveNumbers extends Unfold[String, Int] {
    override val s0: Int = 0
    override def next(s: Int): Option[(String, Int)] = s match {
      case 5 => Unfold.stop
      case n => Unfold.next(s"Item $n", n + 1)
    }
  }

  /** 0,1,2,3,4,5 - like until, we know, CURRENT ELEMENT IS THE LAST */
  object SixNumbers extends UnfoldLoop[String, Int] {
    override val s0: Int = 0
    override def next(s: Int): (String, Option[Int]) = s match {
      case n @ 5 => UnfoldLoop.last(s"Item $n")
      case n     => UnfoldLoop.next(s"Item $n", (n + 1))
    }
  }

  val stream1 = fs2.Stream
    .unfold(FiveNumbers.s0) { FiveNumbers.next }
    .covary[IO]
    .compile
    .toList

  val stream2 = fs2.Stream
    .unfoldLoop(SixNumbers.s0) { SixNumbers.next }
    .covary[IO]
    .compile
    .toList

  object TenUniqueRandoms extends UnfoldLoop[Set[Int], Set[Int]] {
    override val s0: Set[Int] = Set.empty
    override def next(s: Set[Int]): (Set[Int], Option[Set[Int]]) = {
      val next = Random.nextInt(15)
      val newSet = s + next
      newSet.size match {
        case 10 => UnfoldLoop.last(newSet)
        case _  => UnfoldLoop.next(newSet, newSet)
      }
    }
  }

  object TenUniqueRandoms2 extends UnfoldLoop[Option[Set[Int]], Set[Int]] {
    override val s0: Set[Int] = Set.empty
    override def next(s: Set[Int]): (Option[Set[Int]], Option[Set[Int]]) = {
      val next = Random.nextInt(15)
      val newSet = s + next
      newSet.size match {
        case 10 => UnfoldLoop.last(newSet.some)
        case _  => UnfoldLoop.next(none, newSet)
      }
    }
  }

  val stream3 = fs2.Stream
    .unfoldLoop(TenUniqueRandoms.s0)(TenUniqueRandoms.next)
    .covary[IO]
    .metered(2.seconds)
    .evalTap(x => IO(println(x)))
    .compile
    .last

  val stream4 = fs2.Stream
    .unfoldLoop(TenUniqueRandoms2.s0)(TenUniqueRandoms2.next)
    .covary[IO]
    .metered(2.seconds)
    .evalTap(x => IO(println(x)))
    .unNone
    .compile
    .last

  override def run: IO[Unit] = for {
    last <- stream4
    _    <- IO(println(last))
  } yield ()

}
