package nomicon.ch02

import zio._

object ZApp2 extends App {

  // has Error type
  lazy val readInt: IO[NumberFormatException, Int] = ???

  // error type is propagated
  lazy val readAndSumTwoInts: ZIO[Any, NumberFormatException, Int] =
    for {
      x <- readInt
      y <- readInt
    } yield x + y

  // error caught, no error type
  lazy val recovered1: ZIO[Any, Nothing, String] =
    readAndSumTwoInts.fold(
      _ => "non Int given", 
      r => s"result is $r"
    )

  class Ex1 extends RuntimeException
  class Ex2 extends RuntimeException
  
  lazy val one: ZIO[Any, Ex1, Int] = ???
  lazy val two: ZIO[Any, Ex2, Int] = ???
  
  lazy val three: ZIO[Any, RuntimeException, Int] = for {
    a <- one
    b <- two
  } yield a + b
  
  
  val done = ZIO.effect(println("Done!"))

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    done.exitCode

}
