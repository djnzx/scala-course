package rtj_cats.wr

import cats.Id
import cats.data.Writer
import cats.data.WriterT

object Writer1App extends App {

  val wA = Writer(Vector("A1, A2"), 10)
  val wB = Writer(Vector("B1, B2"), 20)

  /** left type combined by Semigroup */
  val wC: WriterT[Id, Vector[String], Int] = for {
    a <- wA
    b <- wB
  } yield a + b

  wA.reset

  pprint.pprintln(wC.run)
}
