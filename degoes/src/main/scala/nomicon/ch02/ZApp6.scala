package nomicon.ch02

import java.io.IOException

import zio._
import zio.console._

object ZApp6 extends App {
  
  def echo = for {
    _ <- console.putStr("Enter the number: ")
    s <- console.getStrLn
    _ <- console.putStrLn(s"You have entered: $s")
  } yield ()
  
  /** this stuff is interpreted under the hood without stack problem */
  def echo_loop: ZIO[Console, IOException, Nothing] = echo *> echo_loop

  val readInt: RIO[Console, Int] = for {
    _    <- console.putStr("Enter the number: ")
    line <- console.getStrLn
    int  <- ZIO.effect(line.toInt) // just generic throwable
  } yield int
  
  def numberOrRetry: ZIO[Console, Nothing, Int] =
    readInt
      .orElse(putStrLn("Non int given") *> numberOrRetry)
  
  val readIntAndPrint = for {
    i <- numberOrRetry
    _ <- putStrLn(s"Valid number is entered: $i")
  } yield ()
  
  val app = readIntAndPrint
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
