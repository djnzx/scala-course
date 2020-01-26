package testable_ideas

object Testable05 extends App {

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

  trait Program[A] {
    def run: Unit
  }
  object Program {
    def apply[A](implicit instance: Program[A]): Program[A] = instance
  }

  case class Prod() {
    def prod = println("PROD")
  }
  case class Test() {
    def test = println("TEST")
  }

  object Prod {
    implicit val rand_prod: Random[Prod] = new Random[Prod] {
      override def nextInt(upper: Int): Int = scala.util.Random.nextInt(20)+1
    }
    implicit val console_prod: Console[Prod] = new Console[Prod] {
      override def printLn(s: String): Unit = scala.Console.println(s)
    }
    implicit val program_prod: Program[Prod] = new Program[Prod] {
      override def run: Unit = println("Prod version run")
    }
  }

  object Test {
    implicit val rand_test: Random[Test] = new Random[Test] {
      // predefined values for random
      private val values: Seq[Int] = List(11,22,33,44,55)
      var index = 0
      override def nextInt(upper: Int): Int = {
        val idx = index
        index = (index + 1) % values.length
        values(idx)
      }
    }
    implicit val console_test = new Console[Test] {
      // our storage for `printed lines`
      private val capturedLines: scala.collection.mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer()

      override def printLn(s: String): Unit = capturedLines.addOne(s)
      def captured: Seq[String] = capturedLines.toSeq
    }
    implicit val program_test: Program[Test] = new Program[Test] {
      override def run: Unit = println("Test version run")
    }
  }

  def app[E: Random: Console: Program]: Program[E] = {
    // wiring with appropriate implementation based on type
    def nextInt: Int = Random[E].nextInt(100)
    def printLn(s: String): Unit = Console[E].printLn(s)

    val randoms = 1 to 5 map (_ => nextInt)
    printLn(randoms.mkString(","))
    Program[E]
  }

  // running prod version
  app[Prod].run
  // running test version
  app[Test].run
  // accessing to captured data from test version
  println(Test.console_test.captured)

}
