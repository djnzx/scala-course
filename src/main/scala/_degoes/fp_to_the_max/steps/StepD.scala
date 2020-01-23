package _degoes.fp_to_the_max.steps

object StepD extends App {

  // let's represent our interaction via case class, and make it typed by return type
  final case class IO[A](run: () => A)
  // let's add map / flatMap operations!
  { me =>
    def map[B](f: A => B): IO[B] = {
      val a: A = me.run()
      val b: B = f(a)
      IO.of(b)
    }
    def flatMap[B](f: A => IO[B]): IO[B] = {
      val a: A = me.run()
      val b: IO[B] = f(a)
      b
    }
  }

  // let's create companion class to create effects easily
  object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)
  }

  // let's rewrite our functions
  def pline(line: String): IO[Unit] =   IO.of(scala.Console.out.println(line))
  def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())

  // and we can rewrite our app in following manner
  lazy val app: IO[Unit] = for {
    _    <- pline("Hi! What's your name?")
    name <- rline()
    _    <- pline(s"Hello, $name")
  } yield ()

  println("====")
  app.run()
  println("====")

  /**
    * better,
    * - we made our app fully composable
    *
    * but still:
    * - no way to test / mock / automatic test
    * - no way to reproduce without user interaction
    */
}
