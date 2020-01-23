package _degoes.fp_to_the_max.steps

object StepG2 extends App {

  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline()            : F[String]
  }

  object Console {
    def apply[F[_]](implicit instance: Console[F]): Console[F] = instance
  }

  final case class IO[A](run: () => A) {
    def map[B]   (f: A => B)    : IO[B] = IO.of(f(run()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO.of(f(run()).run())
  }

  trait Program[F[_]] { me =>
    def map   [A, B](fa: F[A], f: A => B)   : F[B]
    def flatMap[A, B](fa: F[A], f: A => F[B]): F[B]
  }

  implicit class ProgramSyntax[F[_]: Program, A](fa: F[A]) {
    def map[B](f: A => B)(implicit pf: Program[F]): F[B] = pf.map(fa, f)
    def flatMap[B](f: A => F[B])(implicit pf: Program[F]): F[B] = pf.flatMap(fa, f)
  }

  object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)

    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def pline(line: String): IO[Unit]   = IO.of(scala.Console.out.println(line))
      override def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())
    }

    implicit val programIO: Program[IO] = new Program[IO] {
      override def map   [A, B](fa: IO[A], f: A => B)    : IO[B] = fa.map(f)
      override def flatMap[A, B](fa: IO[A], f: A => IO[B]): IO[B] = fa.flatMap(f)
    }
  }

  def pline[F[_]: Console](line: String): F[Unit]   = Console[F].pline(line)
  def rline[F[_]: Console]()            : F[String] = Console[F].rline()

  def app[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline("Enter number A")
    n1   <- rline()
    _    <- pline("Enter number B")
    n2   <- rline()
    sum = n1.toInt + n2.toInt
    _    <- pline(s"Sum = $sum")
  } yield ()

  app[IO].run()
}
