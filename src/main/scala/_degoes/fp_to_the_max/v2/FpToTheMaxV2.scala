package _degoes.fp_to_the_max.v2

class FpToTheMaxV2 {

  trait Random[F[_]] {
    def nextInt(upper: Int): F[Int]
  }

  object Random {
    def apply[F[_]](implicit instance: Random[F]): Random[F] = instance
  }

  trait Console[F[_]] {
    // maybe it would be better if we allow the only certain message to be printed
    def putStrLn(line: String): F[Unit]
    def getStrLn(): F[String]
  }

  object Console {
    def apply[F[_]](implicit instance: Console[F]): Console[F] = instance
  }

  trait Program[F[_]] {
    def finish[A](a: => A): F[A]
    def chain[A, B](fa: F[A], afb: A => F[B]): F[B]
    def map  [A, B](fa: F[A],  ab: A => B   ): F[B]
  }

  // ability to use map / flatMap
  implicit class ProgramSyntax[F[_], A](fa: F[A]) {
    def map[B](fab: A => B)(implicit fp: Program[F]): F[B] = fp.map(fa, fab)
    def flatMap[B](fafb: A => F[B])(implicit fp: Program[F]): F[B] = fp.chain(fa, fafb)
  }

  case class IO[A](body: () => A) { self =>
    def map[B]   (f: A => B    ): IO[B] = IO(() => f(self.body()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.body()).body())
  }

  object IO {
    def point[A](a: => A): IO[A] = IO( () => a)

    implicit val ProgramIO: Program[IO] = new Program[IO] {
      override def finish[A](a: => A): IO[A] = IO.point(a)
      override def chain[A, B](fa: IO[A], afb: A => IO[B]): IO[B] = fa.flatMap(afb)
      override def map[A, B](fa: IO[A], ab: A => B): IO[B] = fa.map(ab)
    }

    implicit val ConsoleIO: Console[IO] = new Console[IO] {
      override def putStrLn(line: String): IO[Unit] = IO( () => println(line) )
      override def getStrLn(): IO[String] = IO( () => scala.io.StdIn.readLine() )
    }

    implicit val RandomIO: Random[IO] = new Random[IO] {
      override def nextInt(upper:  Int): IO[Int] = IO(() => scala.util.Random.nextInt(upper))
    }
  }

  case class TestIO[A](run: TestData => (TestData, A)) { self =>
    def map[B](fab: A => B): TestIO[B] = TestIO(t => self.run(t) match { case (t, a) => (t, fab(a)) })
    def flatMap[B](afb: A => TestIO[B]): TestIO[B] = TestIO(t => self.run(t) match { case (t, a) => afb(a).run(t) })
    def eval(t: TestData): TestData = run(t)._1
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
      override def putStrLn(line: String): TestIO[Unit] = TestIO(t => t.putStrLn(line))
      override def getStrLn(): TestIO[String] = TestIO(t => t.getStrLn)
    }

    // mock
    implicit val RandomTestIO: Random[TestIO] = new Random[TestIO] {
      override def nextInt(upper:  Int): TestIO[Int] = TestIO(t => t.nextInt(upper))
    }
  }

  def finish[F[_], A](a: => A)(implicit fp: Program[F]): F[A] = fp.finish(a)

  // the responsibility is to pick appropriate implementation from implicits
  def nextInt[F[_]](upper: Int)(implicit F: Random[F]): F[Int] = Random[F].nextInt(upper)
  // or
  def nextInt2[F[_]: Random](upper: Int): F[Int] = Random[F].nextInt(upper)

  // the responsibility is to pick appropriate implementation from implicits
  def putStrLn[F[_]: Console](line: String): F[Unit] = Console[F].putStrLn(line)

  // the responsibility is to pick appropriate implementation from implicits
  def getStrLn[F[_]: Console](): F[String] = Console[F].getStrLn()

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

  def parseInt_(s: String): Option[Int] = scala.util.Try(s.toInt).toOption

  def printResults[F[_]: Console](input: String, num: Int, name: String): F[Unit] =
    parseInt_(input).fold(
      putStrLn("You didn't enter a number")
    )(response =>
      if (response == num) putStrLn(s"You guessed right, $name!")
      else putStrLn(s"You guessed wrong, $name, the number was:$num")
    )

  def gameLoop[F[_]: Program: Random: Console](name: String): F[Unit] =
    for {
      num   <- nextInt(5).map(_ + 1)
      _     <- putStrLn(s"Dear, $name, guess a number 1..5:")
      input <- getStrLn()
      _     <- printResults(input, num, name)
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

  def mainIO: IO[Unit] = main[IO]

  def mainTestIO: TestIO[Unit] = main[TestIO]

  case class TestData(input: List[String], output: List[String], nums: List[Int]) {
    def putStrLn(line: String): (TestData, Unit) = (copy(output = line :: output), ())
    def getStrLn: (TestData, String) = (copy(input = input.drop(1)), input.head)
    def nextInt(upper: Int): (TestData, Int) = (copy(nums = nums.drop(1)), nums.head)
    def showResults = output.reverse.mkString("\n")
  }

  var testDataset = TestData(
    input = "Alex" :: "1" :: "n" :: Nil,
    output = Nil,
    nums = 0 :: Nil
  )

  def runTest: String = mainTestIO.eval(testDataset).showResults

}
