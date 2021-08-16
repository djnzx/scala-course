package catsx.reader

import cats.data.Reader

object ReaderFlatMap extends App {

  val r1 = Reader[Int, String](_.toString)
  val r2 = Reader[Int, Double](_.toDouble + math.random())
  val r3 = Reader[Int, Boolean](_ > 1)

  val r: Reader[Int, (String, Double, Boolean)] = for {
    a <- r1
    b <- r2
    c <- r3
  } yield (a, b, c)

  println(r(10))
}
