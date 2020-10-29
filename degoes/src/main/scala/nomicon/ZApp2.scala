package nomicon

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
    readAndSumTwoInts.fold(_ => "non Int given", r => s"result is $r")

  // passing dependencies
  trait Res {
    def r: Int
  }
  
  val r1: Res = new Res {
    override def r: Int = 1
  }

  val r2: Res = new Res {
    override def r: Int = 2
  }
  
  val zr1: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r1)
  
  val zr2: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r2)
  
  
  val done = ZIO.effect(println("Done!"))
  
  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    done.exitCode
  
}
