package cats

import cats.syntax.either._

object C143Semi extends App {
  def parseInt(s: String): Either[String, Int] =
    Either.catchOnly[NumberFormatException](s.toInt)
      .leftMap(_ => s"couldn't convert: $s")

  val r = for {
    a <- parseInt("a")
    b <- parseInt("b")
    c <- parseInt("c")
  } yield a + b + c

  println(r)

}
