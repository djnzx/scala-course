package testable_ideas

object Testable04 extends App {

  trait Random[A] {
    def nextInt(upper: Int): Int
  }
  object Random {
    def apply[A](implicit instance: Random[A]): Random[A] = instance
  }

  trait Console[A] {
    def printLn(s: String): Unit
  }
  object Console {
    def apply[A](implicit instance: Console[A]): Console[A] = instance
  }

  type Environment[A] = Random[A] with Console[A]

  case class Prod()
  case class Test()

  object Prod {
    implicit val env_prod: Environment[Prod] = new Random[Prod] with Console[Prod] {
      override def nextInt(upper: Int): Int = scala.util.Random.nextInt(20)+1
      override def printLn(s: String): Unit = scala.Console.println(s)
    }
  }

  object Test {
    implicit val env_test = new Random[Test] with Console[Test] {
      // predefined values for random
      private val values: Seq[Int] = List(11,22,33,44,55)
      // our storage for `printed lines`
      private val capturedLines: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer()

      var index = 0
      override def nextInt(upper: Int): Int = {
        val idx = index
        index = (index + 1) % values.length
        values(idx)
      }

      override def printLn(s: String): Unit = capturedLines.addOne(s)
      def captured: Seq[String] = capturedLines.toSeq
    }

  }

  def app[E: Random: Console] = {
    // wiring with appropriate implementation based on type
    def nextInt: Int = Random[E].nextInt(100)
    def printLn(s: String): Unit = Console[E].printLn(s)

    val randoms = 1 to 5 map (_ => nextInt)
    printLn(randoms.mkString(","))
  }

  // running prod version
  app[Prod]
  // running test version
  app[Test]
  // accessing to captured data from test version
  println(Test.env_test.captured)

  /**
    * everything is OK
    * but we used only functionality of
    * complement Object implicit resolution
    */

}
