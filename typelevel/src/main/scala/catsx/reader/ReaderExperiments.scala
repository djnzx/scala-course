package catsx.reader

import cats.{Id, ~>}
import cats.data.{Kleisli, Reader, ReaderT}
import cats.implicits._

object ReaderExperiments extends App {

  val r1: Reader[Int, Int] = Reader[Int, Int](_ + 1)

  val r2 = r1.map(_ + 1)

  println(r2.run(1)) // 3

  val r3 = r1.local[String](_.toInt)

  println(r3.run("1")) // 3

  type ParseResult[A] = Either[Throwable, A]

  val r0 = Reader[String, ParseResult[Int]](s => Either.catchNonFatal(s.toInt))
  val r4 = ReaderT[ParseResult, String, Int](s => Either.catchNonFatal(s.toInt))
  println(r4("50"))  // Right(50)
  println(r4("50x")) // Left(java.lang.NumberFormatException: For input string: "50x")

  val r5: Kleisli[Id, Int, Option[Int]] = r1.map[Option[Int]](Some(_))
  val r6: Kleisli[Option, Int, Int] = r1.mapK(new (Id ~> Option) {
    override def apply[A](fa: Id[A]): Option[A] = Some(fa)
  })
  val r7: Kleisli[List, Int, Int] = r6.mapF(_.toList)


}
