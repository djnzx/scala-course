package fwd

object ForwardReferenceProblem extends App {

  val x = y
  val y = 5

  println(s"x=$x")
  println(s"y=$y")

  /** compilation fails 1:
    *
    * def noProblem1() = {
    *   val a = b
    *   val b = 1
    * }
    *
    */

  /** compilation fails 1:
    *
    * def noProblem2() = for {
    *   b <- Option(a)
    *   a <- Option(1)
    * } yield (a, b)
    *
    */

}
