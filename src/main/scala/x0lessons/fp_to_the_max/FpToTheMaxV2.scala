package x0lessons.fp_to_the_max

import scala.language.higherKinds

object FpToTheMaxV2 extends App {

  def checkContinue[F[_]: Program: Console](name: String): F[Boolean] =
    for {
      _     <- putStrLn(s"Do you want to continue, $name?")
      input <- getStrLn().map(_.toLowerCase())
      cont  <- input match {
                  case "y" => finish(true)
                  case "n" => finish(false)
                  case _   => checkContinue(name)
               }
    } yield cont

  def gameLoop[F[_]: Program: Random: Console](name: String): F[Unit] =
    for {
      num   <- nextInt(5).map(_ + 1)
      _     <- putStrLn(s"Dear, $name, gueass a number 1..5:")
      input <- getStrLn()
      _     <- parseInt(input).fold(
                 putStrLn("You didn't enter a number")
               )(guess =>
                 if (guess == num) putStrLn(s"You guessed right, $name!")
                 else putStrLn(s"You guessed wrong, $name, the number was:$num")
               )
      cont  <- checkContinue(name)
      _     <- if (cont) gameLoop(name) else finish(())

    } yield ()

  def main[F[_]: Program: Random: Console]: F[Unit] =
    for {
      _    <- putStrLn("What is your name?")
      name <- getStrLn()
      _    <- putStrLn(s"Hello, $name, welcome!")
      _    <- gameLoop(name)
    } yield ()

  def mainIO = main[IO].core()

  mainIO
}
