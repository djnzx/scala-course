package _inject_test_ideas.step01

/**
  * The task:
  * - to generate and print 5 random numbers in the range 1..20
  *
  * The problem:
  * - how to TEST that program
  * - how to provide predictably numbers for future comparision
  */
object Testable01 extends App {

  val randoms: Seq[Int] = 1 to 5 map(n => scala.util.Random.nextInt(20)+1)
  println(randoms)

}
