package degoes.fp_to_the_max.v1

import scala.io.StdIn.readLine
import scala.util.{Random, Try}

object FpToTheMaxV1 extends App {

  // (a: => A) - lazyness
  // https://stackoverflow.com/questions/4543228/whats-the-difference-between-and-unit
  case class IO[A](core: () => A) { self =>
    def map[B](f: A => B): IO[B] = IO(() => f(self.core()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.core()).core())
  }

  object IO {
    def of[A](a: => A): IO[A] = IO(() => a)
  }

  def parseInt(s: String): Option[Int] = Try(s.toInt).toOption
  // interaction representation
  def putStrLn(line: String): IO[Unit] = IO( () => println(line) )
  def getStrLn(): IO[String] = IO( () => readLine() )
  def nextInt(upper: Int): IO[Int] = IO(() => Random.nextInt(upper))

  def checkContinue(name: String): IO[Boolean] =
    for {
      _     <- putStrLn(s"Do you want to continue, $name?")
      input <- getStrLn().map(_.toLowerCase())
      cont  <- input match {
                  case "y" => IO.of(true)
                  case "n" => IO.of(false)
                  case _   => checkContinue(name)
               }
    } yield cont

  def gameLoop(name: String): IO[Unit] =
    for {
      num   <- nextInt(5).map(_ + 1)
      _     <- putStrLn(s"Dear, $name, guess a number 1..5:")
      input <- getStrLn()
      _     <- parseInt(input).fold[IO[Unit]](
                 // empty case
                 putStrLn("You didn't enter a number")
               )(guess =>
                 // non-empty case
                 if (guess == num) putStrLn(s"You guessed right, $name!")
                 else putStrLn(s"You guessed wrong, $name, the number was:$num")
               )
      cont  <- checkContinue(name)
      _     <- if (cont) gameLoop(name) else IO.of(())

    } yield ()

  def main: IO[Unit] =
    for {
      _    <- putStrLn("What is your name?")
      name <- getStrLn()
      _    <- putStrLn(s"Hello, $name, welcome!")
      _    <- gameLoop(name)
    } yield ()

  main.core()
}
