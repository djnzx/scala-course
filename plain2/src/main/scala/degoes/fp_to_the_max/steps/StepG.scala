package degoes.fp_to_the_max.steps

/**
  * going to make Them polymorphic
  */
object StepG extends App {

  // 1. interface to interact with console
  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline()            : F[String]
  }
  // 2. corresponding object to pick appropriate instance
  object Console {
    def apply[F[_]](implicit instance: Console[F]): Console[F] = instance
  }

  // let's represent our interaction via case class, and make it typed by return type
  final case class IO[A](run: () => A){ me =>
    def map[B]   (f: A => B)    : IO[B] = IO.of(f(me.run()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO.of(f(me.run()).run())
    // can be written shorter: `f(me.run())`, but we need laziness
  }

  // let's create companion class to create effects easily
  object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)

    // 3. Implementation for Console[IO]
    // 4. Make it implicit for: Console[IO].pline()
    // 5. And put it into object IO for proper resolving
    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def pline(line: String): IO[Unit]   = IO.of(scala.Console.out.println(line))
      override def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())
    }
    // G4. Implicit instance of ProgramIO
    implicit val programIO: Program[IO] = new Program[IO] {
      override def map   [A, B](fa: IO[A], f: A => B)    : IO[B] = fa.map(f)
      override def flatMap[A, B](fa: IO[A], f: A => IO[B]): IO[B] = fa.flatMap(f)
    }
  }

  // 6. rewrite our functions and tie them to specific Console implementation
  // G5. generalize our functions
  def pline[F[_]: Console](line: String): F[Unit]   = Console[F].pline(line)
  def rline[F[_]: Console]()            : F[String] = Console[F].rline()

  // 7. The task for StepG is to wire Program with appropriate instances of `F` (IO)
  // G1. Program trait
  trait Program[F[_]] {
    // by design
    def map   [A, B](fa: F[A], f: A => B)   : F[B]
    def flatMap[A, B](fa: F[A], f: A => F[B]): F[B]
//    def finish [A]   (a: => A)               : F[A]
  }
  // G2. Corresponding instance picker
//  object Program {
//    def apply[F[_]](implicit instance: Program[F]): Program[F] = instance
//  }
  // G3. Implicit for syntax. can be applied to any F[_]
  implicit class ProgramSyntax[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit pf: Program[F]): F[B] = pf.map(fa, f)
    def flatMap[B](f: A => F[B])(implicit pf: Program[F]): F[B] = pf.flatMap(fa, f)
  }

  // and we can compose them!
  // here we use map/flatMap from IO case class
  // G6. change type IO[Unit] to
  // Console for: `pline` and `rline` because they are `pline[F[_]: Console]`
  // Program - for ProgramSyntax(map/flatmap), because `def map[B](f: A => B)(implicit pf: Program[F]): F[B]`
  def app[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline("Hi! What's your name?")
    name <- rline()
    _    <- pline(s"Hello, $name")
  } yield ()

  // G7.
  def program[F[_]: Program: Console]: F[Unit] = for {
    _ <- app
    _ <- app
  } yield ()

  println("====")
  // G8.
  program[IO].run()
  println("====")
}
