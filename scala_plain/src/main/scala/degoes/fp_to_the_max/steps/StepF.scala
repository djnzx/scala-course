package degoes.fp_to_the_max.steps

/**
  * going to make Them polymorphic
  */
object StepF extends App {

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
  }

  // 6. rewrite our functions and tie them to specific Console implementation
  def pline(line: String): IO[Unit] =   Console[IO].pline(line)
  def rline()            : IO[String] = Console[IO].rline()

  // 7. The task for StepG is to wire Program with appropriate instances of `F` (IO)

  // and we can compose them!
  def app: IO[Unit] = for {
    _    <- pline("Hi! What's your name?")
    name <- rline()
    _    <- pline(s"Hello, $name")
  } yield ()

  lazy val program: IO[Unit] = for {
    _ <- app
    _ <- app
  } yield ()

  println("====")
  program.run()
  println("====")
}
