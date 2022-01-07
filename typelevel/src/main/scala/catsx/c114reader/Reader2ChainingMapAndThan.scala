package catsx.c114reader

import cats.data.Reader
import utils.Timed.printTimed

object Reader2ChainingMapAndThan extends App {

  val f1: Int => Int = a => { Thread.sleep(2000); a + 1 }
  val f2: Int => Int = a => { Thread.sleep(2000); a * 2 }

  val r1: Reader[Int, Int] = Reader(f1)
  val r2: Reader[Int, Int] = Reader(f2)

  /** we can chain Reader and function */
  val r12a: Reader[Int, Int] = r1 map f2

  /** we can combine two Readers */
  val r12b: Reader[Int, Int] = r1 andThen r2

  /** flatMap actually is a product */
  val r12c: Reader[Int, (Int, Int)] = for {
    a1 <- r1
    a2 <- r2
  } yield (a1, a2)

  println("running map:")
  printTimed(
    r12a(100) == 202,
  )

  println("running andThen:")
  printTimed(
    r12b(100) == 202,
  )

  println("running flatMap:")
  printTimed(
    r12c(100) == (101, 200),
  )

}
