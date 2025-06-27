package eff

import cats.effect._
import cats.implicits._

class UsingErrorChannelExample[F[_]: Async] {

  case class E1(x: Int)    extends Exception
  case class E2(y: String) extends Exception

  implicit class HandleExOps[A](fa: F[A]) {
    def handle: F[String] = fa
      .map(_.toString)
      .recover {
        case E1(x) => s"handled E1($x)"
        case E2(x) => s"handled E2($x)"
      }
  }

  def code1 = for {
    x <- 1.pure[F]
    y <- 2.pure[F]
  } yield x + y

  val block1 = code1.handle

  // the main channel is - Int
  // the error channel - any exception
  // with recover we can convert some of them to result also

  def code2 = for {
    x <- E2("boom!").raiseError[F, Int]
    y <- 2.pure[F]
  } yield x + y

  val block2 = code2.handle

  def code3 = for {
    x <- 2.pure[F]
    y <- E1(999).raiseError[F, Int]
  } yield x + y

  val block3 = code3.handle

  def code4 = for {
    x <- 2.pure[F]
    y <- E1(999).raiseError[F, Int]
  } yield x + y

  // can be overwritten. this way -- error will be swallowed
  val block4 = code4.handle.as("-13")

  def code5 = for {
    x <- E1(999).raiseError[F, Int]
    y <- E2("888").raiseError[F, Int]
  } yield 22

  // can't be overwritten. error has a priority
  val block5 = code5.handle

}
