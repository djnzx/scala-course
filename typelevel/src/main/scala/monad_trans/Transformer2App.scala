package monad_trans

import cats.implicits._
import cats.data.OptionT

object Transformer2App {
  
  val io1: Either[String, Option[Int]] = Right(Option(123))
  val io2: Either[String, Option[Int]] = Right(Option(234))

  /** plain */
  val r1: Either[String, Option[Int]]  = for {
    o1 <- io1
    o2 <- io2
  } yield for {
    i1 <- o1
    i2 <- o2
    sum = i1 + i2
  } yield sum
  
  /** Monad transformers syntax */
  val r2: Either[String, Option[Int]] = (for {
    i1 <- OptionT(io1)
    i2 <- OptionT(io2)
    sum = i1+i2
  } yield sum).value
  
}
