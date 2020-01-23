package _degoes.fp_to_the_max.v2

class FpToTheMaxV2 {

  trait Random[F[_]] {
    def nextInt(upper: Int): F[Int]
  }
  object Random {
    def apply[F[_]](implicit instance: Random[F]): Random[F] = instance
  }

  trait Console[F[_]] {
    def putStrLn(line: String): F[Unit]
    def getStrLn(): F[String]
  }
  object Console {
    def apply[F[_]](implicit instance: Console[F]): Console[F] = instance
  }

  trait Program[F[_]] {
    def finish[A](a: => A): F[A]
    def map  [A, B](fa: F[A],  ab: A => B   ): F[B]
    def chain[A, B](fa: F[A], afb: A => F[B]): F[B]
  }
  object Program {
    def apply[F[_]: Program]: Program[F] = implicitly[Program[F]]
  }

  implicit class ProgramSyntax[F[_], A](fa: F[A]) {
    def map[B]   (fab:  A => B   )(implicit fp: Program[F]): F[B] = fp.map(fa, fab)
    def flatMap[B](fafb: A => F[B])(implicit fp: Program[F]): F[B] = fp.chain(fa, fafb)
  }

  case class IO[A](run: () => A) { me =>
    def map[B]   (f: A => B    ): IO[B] = IO.of(f(me.run()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO.of(f(me.run()).run())
//    def map[B]   (f: A => B    ): IO[B] = IO(() => f(me.run()))
//    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(me.run()).run())
  }

  object IO {
    def of[A](a: => A): IO[A] = new IO( () => a)

    implicit val randomIO: Random[IO] = new Random[IO] {
      override def nextInt(upper:  Int):   IO[Int]    = IO(() => scala.util.Random.nextInt(upper))
    }

    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def putStrLn(line: String): IO[Unit]   = IO( () => scala.Console.println(line) )
      override def getStrLn():             IO[String] = IO( () => scala.io.StdIn.readLine() )
    }

    implicit val programIO: Program[IO] = new Program[IO] {
      override def finish[A]   (a: => A)                   : IO[A] = IO( () => a)
      override def chain[A, B](fa: IO[A], afb: A => IO[B]): IO[B] = fa.flatMap(afb)
      override def map[A, B]  (fa: IO[A], ab: A => B)     : IO[B] = fa.map(ab)
    }
  }

  case class TestIO[A](run: TestData => (TestData, A)) { me =>
    def map[B](fab: A => B): TestIO[B] = TestIO(t => {
      // De Goez syntax
//      self.run(t) match { case (t, a) => (t, fab(a)) }
      // decomposed syntax
      val rez: (TestData, A) = me.run(t)
      val b: B = fab(rez._2)
      (rez._1, b)
    })
    def flatMap[B](afb: A => TestIO[B]): TestIO[B] = TestIO(t =>
      me.run(t) match { case (t, a) => afb(a).run(t) })
    // run and return the first part
    def eval(t: TestData): TestData = run(t)._1
  }

  object TestIO {
    implicit val randomTestIO: Random[TestIO] = new Random[TestIO] {
      override def nextInt(upper:  Int): TestIO[Int] = TestIO(t => t.nextInt(upper))
    }

    implicit val consoleTestIO: Console[TestIO] = new Console[TestIO] {
      override def putStrLn(line: String): TestIO[Unit]   = TestIO(t => t.putStrLn(line))
      override def getStrLn()            : TestIO[String] = TestIO(t => t.getStrLn)
    }

    implicit val programTestIO: Program[TestIO] = new Program[TestIO] {
      override def finish[A]   (a: => A)                           : TestIO[A] = TestIO( t => (t, a))
      override def map[A, B]  (fa: TestIO[A], ab: A => B)         : TestIO[B] = fa.map(ab)
      override def chain[A, B](fa: TestIO[A], afb: A => TestIO[B]): TestIO[B] = fa.flatMap(afb)
    }
  }

  // syntax #1
  def finish1[F[_], A]         (a: => A)(implicit fp: Program[F]): F[A] = fp.finish(a)
  // syntax #2
  def finish2[F[_]: Program, A](a: => A):                          F[A] = implicitly[Program[F]].finish(a)

  // the responsibility is to pick appropriate implementation from implicits
  def finish [F[_]: Program, A](a: => A)    : F[A]      = Program[F].finish(a)
  def nextInt [F[_]: Random](upper: Int)   : F[Int]    = Random[F].nextInt(upper)
  def putStrLn[F[_]: Console](line: String): F[Unit]   = Console[F].putStrLn(line)
  def getStrLn[F[_]: Console]()            : F[String] = Console[F].getStrLn()

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
//      num   <- Random[F].nextInt(5).map(_ + 1)
      num   <- nextInt(5).map(_ + 1)
      _     <- putStrLn(s"Dear, $name, guess a number 1..5:")
      input <- getStrLn()
      _     <- printResults(input, num, name)
      cont  <- checkContinue(name)
      _     <- if (cont) gameLoop(name) else finish(())
    } yield ()

  def app[F[_]: Program: Random: Console]: F[Unit] =
    for {
      _    <- putStrLn("What is your name?")
      name <- getStrLn()
      _    <- putStrLn(s"Hello, $name, welcome!")
      _    <- gameLoop(name)
    } yield ()

  /**
    * this is test data for my app
    *
    * @param input - user's input
    * @param output - program's output
    * @param nums - numbers generated by random
    */
  case class TestData(input: List[String], output: List[String], nums: List[Int]) {
    def putStrLn(line: String): (TestData, Unit)   = (copy(output = line :: output), ())
    def getStrLn:               (TestData, String) = (copy(input  = input.drop(1)) , input.head)
    def nextInt(upper: Int):    (TestData, Int)    = (copy(nums   = nums.drop(1))  , nums.head)
    def results: String = output.reverse.mkString("\n")
  }

  def real: IO[Unit] = app[IO]
  def test: TestIO[Unit] = app[TestIO]

}
