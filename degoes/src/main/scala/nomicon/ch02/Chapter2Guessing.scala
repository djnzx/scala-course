package nomicon.ch02

import zio._
import zio.console.Console
import zio.random.Random

object Chapter2Guessing extends App {
  // 18
  def toInt(s: String) = try {
    Right(s.toInt)
  } catch {
    case x: NumberFormatException => Left(x)
  }
  def toIntZIO(s: String) = ZIO.fromEither(toInt(s))
  def readInt = console.getStrLn.flatMap(toIntZIO)
  def readIntOrRetry: ZIO[Console, Nothing, Int] = readInt
    .orElse(console.putStrLn("not a number entered, please retry") *> readIntOrRetry)

  def guessingApp: URIO[Console with Random, Unit] = for {
    guessed <- random.nextIntBetween(1, 4)
    _       <- console.putStr("Enter number:")
    entered <- readIntOrRetry
    _       <- if (guessed == entered) console.putStrLn("You were right")
               else                    console.putStrLn(s"You were wrong ($guessed), again") *> guessingApp
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = guessingApp.exitCode

}
