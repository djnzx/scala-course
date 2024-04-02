package cats101.c077monads

import scala.util.Try

object C078 extends App {
  def parseInt(str: String): Option[Int] = Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap { aNum =>
      parseInt(bStr).flatMap { bNum =>
        divide(aNum, bNum)
      }
    }

  def stringDivideBy_v2(aStr: String, bStr: String): Option[Int] =
    for {
      aNum <- parseInt(aStr)
      bNum <- parseInt(bStr)
      ans <- divide(aNum, bNum)
    } yield ans

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def doSomethingLongRunning: Future[Int] = ???

  def doSomethingElseLongRunning: Future[Int] = ???

  def doSomethingVeryLongRunning: Future[Int] =
    for {
      result1 <- doSomethingLongRunning
      result2 <- doSomethingElseLongRunning
    } yield result1 + result2

  def doSomethingVeryLongRunning2: Future[Int] =
    doSomethingLongRunning.flatMap { result1 =>
      doSomethingElseLongRunning.map { result2 =>
        result1 + result2
      }
    }
}
