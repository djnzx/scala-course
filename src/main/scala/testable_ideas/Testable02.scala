package testable_ideas

/**
  * We introduced trait Random
  * and created two implementations
  * as a consequence we need to rewrite MainApp with dependency Random
  *
  * that's better but too cumbersome:
  * for each and every environment we need to create a lot of instances
  */
object Testable02 extends App {

  trait Random {
    def nextInt(upper: Int): Int
  }

  val random_real: Random = new Random {
    override def nextInt(upper: Int): Int = scala.util.Random.nextInt(20)+1
  }

  val random_mock: Random = new Random {
    val values: Seq[Int] = List(1,3,5,7,9)
    val len: Int = values.length

    var index = 0
    override def nextInt(upper: Int): Int = {
      val idx = index
      index = (index + 1) % len
      values(idx)
    }
  }

  def app(random: Random): Seq[Int] = 1 to 5 map(_ => random.nextInt(20))

  val randoms_real = app(random_real)
  val randoms_mock = app(random_mock)

  println(randoms_real)
  println(randoms_mock)



}
