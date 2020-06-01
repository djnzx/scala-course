import cats.effect.IO

package object monad_nested {

  val ioo1: IO[Option[Int]] = IO.pure(Some(1))
  val ioo2: IO[Option[Int]] = IO.pure(Some(2))
  val ioo5: IO[Option[Int]] = IO.pure(Some(5))
  val ioo6: IO[Option[Int]] = IO.pure(Some(6))

  val ioe3: IO[Either[String, Int]] = IO.pure(Right(3))
  val ioe4: IO[Either[String, Int]] = IO.pure(Right(4))

  val f: (Int, Int) => Int = (a, b) => a + b

}
