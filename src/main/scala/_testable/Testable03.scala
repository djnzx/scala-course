package _testable

/**
  * We introduced trait Random
  * and created two implementations
  * as a consequence we need to rewrite MainApp with dependency Random
  *
  * that's better but too cumbersome:
  * for each and every environment we need to create a lot of instances
  */
object Testable03 extends App {

  trait Random {
    def nextInt(upper: Int): Int
  }

  trait Console {
    def printLn(s: String): Unit
  }

  trait Debug {
    def captured: Seq[String]
  }

  type Environment = Random with Console
  type TestEnvironment = Random with Console with Debug

  val env_real: Environment = new Random with Console {
    override def nextInt(upper: Int): Int = scala.util.Random.nextInt(20)+1
    override def printLn(s: String): Unit = scala.Console.println(s)
  }

  val env_mock: TestEnvironment = new Random with Console with Debug {
    // predefined values for random
    val values: Seq[Int] = List(1,3,5,7,9)
    // our storage for `printed lines`
    val capturedLines: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer()

    var index = 0
    override def nextInt(upper: Int): Int = {
      val idx = index
      index = (index + 1) % values.length
      values(idx)
    }

    override def printLn(s: String): Unit = capturedLines.addOne(s)
    override def captured: Seq[String] = capturedLines.toSeq
  }

  implicit def itos(v: Int): String = v.toString

  def app(env: Environment): Seq[Int] = 1 to 5 map(_ => env.nextInt(20))
  def out(env: Environment, data: Seq[Int]): Unit = data.foreach(env.printLn(_))

  val randoms_real = app(env_real)
  val randoms_mock = app(env_mock)
  out(env_real, randoms_real)
  out(env_mock, randoms_mock)

  print(env_mock.captured)

  /**
    * but the biggest problem -
    * we need to provide and maintain a lot of duplicated code
    */
}
