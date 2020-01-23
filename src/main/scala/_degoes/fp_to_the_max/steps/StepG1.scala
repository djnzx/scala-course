package _degoes.fp_to_the_max.steps

object StepG1 extends App {
  // 1. interface to interact. F can be IO, TestIO, DevIO, MockIO, etc
  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline()            : F[String]
  }
  // 2. complement object ONLY to pick appropriate instance
  object Console {
    def apply[F[_]](implicit instance: Console[F]): Console[F] = instance
  }
  // 3. Interaction representation (only high order functions)
  final case class IO[A](run: () => A){ me =>
    def map[B]   (f: A => B)    : IO[B] = IO.of(f(me.run()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO.of(f(me.run()).run())
  }
  // 4. The same idea as with Console
  trait Program[F[_]] { me =>
    def map   [A, B](fa: F[A], f: A => B)   : F[B]
    def flatMap[A, B](fa: F[A], f: A => F[B]): F[B]
  }
  //  // 5. Syntax
  implicit class ProgramSyntax[F[_]: Program, A](fa: F[A]) {
    def map[B](f: A => B)(implicit pf: Program[F]): F[B] = pf.map(fa, f)
    def flatMap[B](f: A => F[B])(implicit pf: Program[F]): F[B] = pf.flatMap(fa, f)
  }
  // 6. Implementation
  object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)
    // console instance
    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def pline(line: String): IO[Unit]   = IO.of(scala.Console.out.println(line))
      override def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())
    }
    // program instance
    implicit val programIO: Program[IO] = new Program[IO] {
      override def map   [A, B](fa: IO[A], f: A => B)    : IO[B] = fa.map(f)
      override def flatMap[A, B](fa: IO[A], f: A => IO[B]): IO[B] = fa.flatMap(f)
    }
  }
  // 7. wiring our functions to corresponding implementations
  def pline[F[_]: Console](line: String): F[Unit]   = Console[F].pline(line)
  def rline[F[_]: Console]()            : F[String] = Console[F].rline()
  // 8. writing parts of program
  def app[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline("Hi! What's your name?")
    name <- rline()
    _    <- pline(s"Hello, $name")
  } yield ()

  def appc[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline("Enter number A")
    n1   <- rline()
    _    <- pline("Enter number B")
    n2   <- rline()
    sum = n1.toInt + n2.toInt
    _    <- pline(s"Sum = $sum")
  } yield ()

  // 9. combining them
  def program[F[_]: Program: Console]: F[Unit] = for {
    _ <- app
    _ <- appc
  } yield ()
  // 10. run
  println("-")
  program[IO].run()
  println("-")
}
