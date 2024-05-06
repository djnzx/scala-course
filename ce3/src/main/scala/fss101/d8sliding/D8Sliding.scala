package fss.d8sliding

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import scala.concurrent.duration.DurationInt
import scala.util.Random

/** 1 2 3 4 5 6 7 8 9 10
  *
  * becomes
  *
  * (1, 2, 3)
  * (2, 3, 4)
  * (3, 4, 5)
  * (4, 5, 6)
  * (5, 6, 7)
  * (6, 7, 8)
  * (7, 8, 9)
  */
object D8Sliding extends IOApp.Simple {

  /** no limitation in Scala... */
  def f1(x: Int): String = ???
  def f1(x: Boolean): Double = ???

  val r1: String = f1(1)
  val r2: Double = f1(true)

  val stream4 = fs2.Stream
    .emits(1 to 10)
    .sliding(3)
    .covary[IO]
    .compile
    .toList

  override def run: IO[Unit] = for {
    x <- stream4
    _ <- x.traverse_(x => IO(println(x)))
  } yield ()

}
