package degoes.fp_to_the_max.steps

object StepB extends App {

  def pline(line: String): () => Unit   = () => scala.Console.out.println(line)
  def rline()            : () => String = () => scala.io.StdIn.readLine()

  val p1 = pline("Hi! What's your name?")
  val name = rline()
  lazy val p2 = pline(s"Hello, ${name()}")

  println("====")
  p1()
  p2()
  println("====")

  /**
    * better,
    * - we decoupled combination from running,
    * - all side effects only in lines 13 and 14
    * - can be reused (by calling functions)
    * - we can represent repetitive code as another functions
    *
    * - no way to test / mock / automatic test
    * - no way to reproduce without user interaction
    */
}
