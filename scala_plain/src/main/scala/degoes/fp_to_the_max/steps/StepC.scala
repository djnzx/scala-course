package degoes.fp_to_the_max.steps

object StepC extends App {

  // let's represent our interaction via case class, and make it typed by return type
  final case class IO[A](run: () => A)

  // let's create companion class to create effects easily
  object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)
  }

  // let's rewrite our functions
  def pline(line: String): IO[Unit] =   IO.of(scala.Console.out.println(line))
  def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())

  val p1  : IO[Unit]   = pline("Hi! What's your name?")
  val name: IO[String] = rline()
  lazy val p2 = pline(s"Hello, ${name.run()}")

  println("====")
  p1.run()
  p2.run()
  println("====")

  /**
    * better,
    * - we added level of abstraction
    *
    * but still:
    * - no way to test / mock / automatic test
    * - no way to reproduce without user interaction
    */
}
