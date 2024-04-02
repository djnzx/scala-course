package cats101.c114reader

import cats.{Id, ~>}
import cats.data.{Kleisli, Reader, ReaderT}
import cats.implicits._

object Reader3GeneralizedThoughts extends App {

  /** basic function */
  val r1: Reader[Int, Int] = Reader[Int, Int](_ + 1)

  /** chaining another function to the result */
  val r2 = r1.map(_ + 1)

  println(r2.run(1)) // 3

  /** prepending mapping to the input parameter */
  val r3 = r1.local[String](_.toInt)

  println(r3.run("1")) // 3

  type ParseResult[A] = Either[Throwable, A]

  /** all of them are the same */
  val r41 = Reader[String, ParseResult[Int]](s => Either.catchNonFatal(s.toInt))
  val r42 = ReaderT[ParseResult, String, Int](s => Either.catchNonFatal(s.toInt))
  val r43 = Kleisli[ParseResult, String, Int](s => Either.catchNonFatal(s.toInt))

  println(r41("50"))  // Right(50)
  println(r41("50x")) // Left(java.lang.NumberFormatException: For input string: "50x")

  /** doing that we change result type, not a wrapper */
  val r5: Kleisli[Id, Int, Option[Int]] = r1.map[Option[Int]](Some(_))

  /** doing that we explicitly change the context, by providing F[A] => G[B] */
  val r71: Kleisli[List, Int, Int] = r1.mapF(_.toList)
  val r72: Kleisli[List, Int, String] = r1.mapF(_.toList.map(_.toString))

  def idToOpt[A](a: A): Option[A] = Option(a)
  def optToList[A](oa: Option[A]): List[A] = oa.toList

  val idToOpt:   Id ~> Option   = new (Id ~> Option) {
    override def apply[A](fa: Id[A]): Option[A] = Option(fa)
  }
  val optToList: Option ~> List = new (Option ~> List) {
    override def apply[A](fa: Option[A]): List[A] = fa.toList
  }

  /** doing that we explicitly change the context, by providing F[_] => G[_] */
  val r61: Kleisli[Option, Int, Int] = r1.mapK(idToOpt)
  val r62: Kleisli[List, Int, Int] = r61.mapK(optToList)

}
