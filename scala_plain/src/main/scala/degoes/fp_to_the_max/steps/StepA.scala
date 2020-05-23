package degoes.fp_to_the_max.steps

object StepA extends App {

  scala.Console.out.println("Hi! What's your name?")
  val name = scala.io.StdIn.readLine()
  scala.Console.out.println(s"Hello, $name")

  /**
    * everything is OK, but:
    *
    * - no way to fix the situation after lines 5, 6, 7. because side effects
    * - no way to reuse
    * - no way to test / mock / automatic test
    * - no way to reproduce without user interaction
    */
}
