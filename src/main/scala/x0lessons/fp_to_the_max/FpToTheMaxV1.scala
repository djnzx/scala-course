package x0lessons.fp_to_the_max

import scala.io.StdIn.readLine
import scala.util.{Random, Try}

/**
  * that code from the video
  * https://www.youtube.com/watch?v=sxudIMiOo68
  *
  * problems:
  * - partial functions
  * - exceptions
  * - variable scope !
  *
  * 1. total. for every input we always have an output
  * 2. deterministic. for the same input the same output
  * 3. pure. there is no effect allowed inside the functions
  *
  * IDEA: to represent any interaction with world in some manner
  */

object FpToTheMaxV1 extends App {
  def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

  case class IO[A](core: () => A) { self =>
    def map[B](f: A => B): IO[B] = IO( () => f(self.core()) )
    def flatMap[B](f: A => IO[B]): IO[B] = IO( () => f(self.core()).core())
  }
  object IO {
    def point[A](a: => A): IO[A] = IO( () => a) // pure representation of any value
  }

  // interaction representation
  def putStrLn(line: String): IO[Unit] = IO( () => println(line) )
  def getStrLn(): IO[String] = IO( () => readLine() )
  def nextInt(upper: Int): IO[Int] = IO(() => Random.nextInt(upper))
  def checkContinue(name: String): IO[Boolean] =
    for {
      _     <- putStrLn(s"Do you want to continue, $name?")
      input <- getStrLn().map(_.toLowerCase())
      cont  <- input match {
                  case "y" => IO.point(true)
                  case "n" => IO.point(false)
                  case _   => checkContinue(name)
               }
    } yield cont

  def gameLoop(name: String): IO[Unit] =
    for {
      num   <- nextInt(5).map(_ + 1)
      _     <- putStrLn(s"Dear, $name, gueass a number 1..5:")
      input <- getStrLn()
      _     <- parseInt(input).fold[IO[Unit]](
                 putStrLn("You didn't enter a number")
               )(guess =>
                 if (guess == num) putStrLn(s"You guessed right, $name!")
                 else putStrLn(s"You guessed wrong, $name, the number was:$num")
               )
      cont  <- checkContinue(name)
      _     <- if (cont) gameLoop(name) else IO.point(())

    } yield ()

  def main:IO[Unit] =
    for {
      _    <- putStrLn("What is your name?")
      name <- getStrLn()
      _    <- putStrLn(s"Hello, $name, welcome!")
      _    <- gameLoop(name)
    } yield ()

  main.core()
}
