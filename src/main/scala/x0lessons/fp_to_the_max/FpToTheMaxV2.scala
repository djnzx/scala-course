package x0lessons.fp_to_the_max

import scala.language.higherKinds

object FpToTheMaxV2 extends App {

  case class TestData(input: List[String], output: List[String], nums: List[Int])

  case class TestIO[A](run: TestData => (TestData, A)) { self =>
    def map[B](fab: A => B): TestIO[B] =
      TestIO(t => self.run(t) match { case (t, a) => (t, fab(a)) })
    def flatMap[B](afb: A => TestIO[B]): TestIO[B] =
      TestIO(t => self.run(t) match { case (t, a) => afb(a).run(t) })
  }
  object TestIO {
    def point[A](a: => A): TestIO[A] = TestIO(t => (t, a))

    implicit val ProgramTestIO: Program[TestIO] = new Program[TestIO] {
      override def finish[A](a: => A): TestIO[A] = TestIO.point(a)
      override def chain[A, B](fa: TestIO[A], afb: A => TestIO[B]): TestIO[B] = fa.flatMap(afb)
      override def map[A, B](fa: TestIO[A], ab: A => B): TestIO[B] = fa.map(ab)
    }

    // mock
    implicit val ConsoleTestIO: Console[TestIO] = new Console[TestIO] {
      override def putStrLn(line: String): TestIO[Unit] = ???
      override def getStrLn(): TestIO[String] = ???
    }

    // mock
    implicit val RandomTestIO: Random[TestIO] = new Random[TestIO] {
      override def nextInt(upper:  Int): TestIO[Int] = ???
    }
  }

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

  def mainTestIO = main[TestIO]
}
