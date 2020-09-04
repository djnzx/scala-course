package monad_trans

import cats.data.OptionT

object Transformer2App extends App {
  val io1 = Right(Option(123))
  val io2 = Right(Option(234))
  /**
    * plain:
    */
  val r: Either[Nothing, Option[Int]]  = for {
    o1 <- io1
    o2 <- io2
  } yield for {
    i1 <- o1
    i2 <- o2
  } yield i1 + i2
  /**
    * Monad transformers
    */
  val r2: Right[Nothing, Option[Int]] = (for {
    i1 <- OptionT(io1)
    i2 <- OptionT(io2)
  } yield i1 + i2).value
  
}
